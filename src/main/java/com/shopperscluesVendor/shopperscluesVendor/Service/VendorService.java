package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.InventoryDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import com.shopperscluesVendor.shopperscluesVendor.Feing.InventoryClient;
import com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra.ProductRepository;
import com.shopperscluesVendor.shopperscluesVendor.Repository.jpa.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class VendorService {

    private final VendorRepository vendorRepo;
    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, InventoryDTO> inventoryTemplate;
    private final MailService mailService;


    public VendorDTO add_vendor(VendorDTO vendorDTO) {
        log.info("Adding new vendor: {}", vendorDTO);
        Vendor vendor = new Vendor();
        vendor.setName(vendorDTO.getName());
        vendor.setCity(vendorDTO.getCity());
        vendor.setGstNumber(vendorDTO.getGstNumber());
        vendor.setMailId(vendorDTO.getMailId());

        Vendor savedVendor = vendorRepo.save(vendor);
        log.info("Vendor saved: {}", savedVendor);

        List<Product> productList = vendorDTO.getProductList().stream()
                .map(dto -> {
                    Product product = new Product();
                    product.setId(UUID.randomUUID());
                    product.setName(dto.getName());
                    product.setCategory(dto.getCategory());
                    product.setPrice(dto.getPrice());
                    product.setVendorId(savedVendor.getId());
                    product.setQuantity_big(dto.getQuantity_big());

                    // Build InventoryDTO
                    InventoryDTO inventoryDTO = new InventoryDTO();
                    inventoryDTO.setProductId(product.getId().toString());
                    inventoryDTO.setName(product.getName());
                    inventoryDTO.setQuantity(product.getQuantity_big());
                    inventoryDTO.setVendorId(savedVendor.getId());

                    // Send to Kafka
                    log.info("Publishing inventory update to Kafka for product {}", product.getId());
                    inventoryTemplate.send("inventory-topic", inventoryDTO)
                            .thenAccept(result -> {
                                log.info("Kafka message sent successfully for product {}", inventoryDTO.getProductId());

                                String subject = "Product Added Successfully";
                                String body = String.format("""
                Hello %s,

                Your product '%s' has been successfully added to inventory.
                Product ID: %s
                Quantity: %d

                Regards,
                Vendor Management Team
                """,
                                        savedVendor.getName(),
                                        product.getName(),
                                        product.getId(),
                                        product.getQuantity_big());

                                try {
                                    mailService.sendMail(savedVendor.getMailId(), subject, body);
                                    log.info("Notification email sent to {}", savedVendor.getMailId());
                                } catch (Exception e) {
                                    log.error("Failed to send email to vendor {}", savedVendor.getMailId(), e);
                                }
                            })
                            .exceptionally(ex -> {
                                log.error("Failed to send Kafka message for product {}", inventoryDTO.getProductId(), ex);
                                return null;
                            });
                    log.info("Adding product {} to inventory for vendor {}", product.getName(), savedVendor.getId());
//                    inventoryClient.addInventory(product.getId().toString(), product.getName(), product.getQuantity_big(), product.getVendorId());
                    return product;
                }).toList();

        productRepository.saveAll(productList);
        log.info("All products saved for vendor: {}", savedVendor.getId());

        return toDTO(savedVendor, productList);
    }

    public List<Vendor> getAll() {
        log.info("Fetching all vendors");
        List<Vendor> vendors = vendorRepo.findAll();
        log.info("Total vendors fetched: {}", vendors.size());
        return vendors;
    }

    public VendorDTO getVendorById(Long id) {
        log.info("Fetching vendor by ID: {}", id);
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));
        List<Product> products = productRepository.findByVendorId(vendor.getId());
        log.info("Vendor found: {} with {} products", vendor.getName(), products.size());
        return toDTO(vendor, products);
    }

    public VendorDTO updateVendor(Long id, VendorDTO dto) {
        log.info("Updating vendor ID: {} with data: {}", id, dto);
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));

        if (dto.getName() != null) vendor.setName(dto.getName());
        if (dto.getCity() != null) vendor.setCity(dto.getCity());
        if (dto.getGstNumber() != null) vendor.setGstNumber(dto.getGstNumber());

        Vendor updatedVendor = vendorRepo.save(vendor);
        log.info("Vendor updated: {}", updatedVendor);

        if (dto.getProductList() != null && !dto.getProductList().isEmpty()) {
            dto.getProductList().forEach(p -> {
                if (p.getId() == null) {
                    Product product = new Product();
                    product.setId(UUID.randomUUID());
                    product.setName(p.getName());
                    product.setCategory(p.getCategory());
                    product.setPrice(p.getPrice());
                    product.setVendorId(updatedVendor.getId());
                    product.setQuantity_big(p.getQuantity_big());

                    productRepository.save(product);
                    log.info("New product added: {} for vendor {}", product.getName(), updatedVendor.getId());

                    inventoryClient.addInventory(product.getId().toString(), product.getName(), product.getQuantity_big(), product.getVendorId());
                } else {
                    Product existingProduct = productRepository.findById(p.getId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + p.getId()));
                    existingProduct.setName(p.getName() != null ? p.getName() : existingProduct.getName());
                    existingProduct.setPrice(p.getPrice() > 0 ? p.getPrice() : existingProduct.getPrice());
                    existingProduct.setCategory(p.getCategory() != null ? p.getCategory() : existingProduct.getCategory());
                    existingProduct.setQuantity_big(p.getQuantity_big() != null ? p.getQuantity_big() : existingProduct.getQuantity_big());

                    productRepository.save(existingProduct);
                    log.info("Existing product updated: {} for vendor {}", existingProduct.getName(), updatedVendor.getId());

                    inventoryClient.updateInventory(existingProduct.getId(), existingProduct.getQuantity_big());
                }
            });
        }

        List<Product> updatedProducts = productRepository.findByVendorId(updatedVendor.getId());
        log.info("Vendor update complete: {} with {} products", updatedVendor.getId(), updatedProducts.size());
        return toDTO(updatedVendor, updatedProducts);
    }

    public void deleteVendor(Long id) {
        log.info("Deleting vendor with ID: {}", id);
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));

        List<Product> vendorProducts = productRepository.findByVendorId(vendor.getId());
        if (!vendorProducts.isEmpty()) {
            vendorProducts.forEach(p -> {
                log.info("Deleting inventory for product: {}", p.getId());
                inventoryClient.deleteInventory(p.getId());
            });
            productRepository.deleteAll(vendorProducts);
            log.info("Deleted all products for vendor: {}", vendor.getId());
        }

        vendorRepo.delete(vendor);
        log.info("Vendor deleted successfully: {}", id);
    }

    private VendorDTO toDTO(Vendor vendor, List<Product> productList) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setCity(vendor.getCity());
        dto.setGstNumber(vendor.getGstNumber());

        List<ProductDTO> productDTOs = productList.stream()
                .map(p -> {
                    ProductDTO pdto = new ProductDTO();
                    pdto.setId(p.getId());
                    pdto.setName(p.getName());
                    pdto.setPrice(p.getPrice());
                    pdto.setCategory(p.getCategory());
                    pdto.setVendorId(p.getVendorId());
                    pdto.setQuantity_big(p.getQuantity_big());
                    return pdto;
                }).toList();

        dto.setProductList(productDTOs);
        return dto;
    }
}
