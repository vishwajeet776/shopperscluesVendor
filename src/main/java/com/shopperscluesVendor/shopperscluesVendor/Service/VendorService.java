package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import com.shopperscluesVendor.shopperscluesVendor.Feing.InventoryClient;
import com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra.ProductRepository;
import com.shopperscluesVendor.shopperscluesVendor.Repository.jpa.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepo;
    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    // CREATE Vendor with products & add inventory
    public VendorDTO add_vendor(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorDTO.getName());
        vendor.setCity(vendorDTO.getCity());
        vendor.setGstNumber(vendorDTO.getGstNumber());

        Vendor savedVendor = vendorRepo.save(vendor);

        List<Product> productList = vendorDTO.getProductList().stream()
                .map(dto -> {
                    Product product = new Product();
                    product.setId(UUID.randomUUID());
                    product.setName(dto.getName());
                    product.setCategory(dto.getCategory());
                    product.setPrice(dto.getPrice());
                    product.setVendorId(savedVendor.getId());
                    product.setQuantity_big(dto.getQuantity_big());

                    // Add to inventory
                    inventoryClient.addInventory(product.getId().toString(),product.getName(), product.getQuantity_big(), product.getVendorId());

                    return product;
                }).toList();

        productRepository.saveAll(productList);

        return toDTO(savedVendor, productList);
    }

    // GET all vendors
    public List<Vendor> getAll() {
        return vendorRepo.findAll();
    }

    // GET vendor by ID
    public VendorDTO getVendorById(Long id) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));
        List<Product> products = productRepository.findByVendorId(vendor.getId());
        return toDTO(vendor, products);
    }

    // UPDATE vendor & products + sync inventory
    public VendorDTO updateVendor(Long id, VendorDTO dto) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));

        if (dto.getName() != null) vendor.setName(dto.getName());
        if (dto.getCity() != null) vendor.setCity(dto.getCity());
        if (dto.getGstNumber() != null) vendor.setGstNumber(dto.getGstNumber());

        Vendor updatedVendor = vendorRepo.save(vendor);

        if (dto.getProductList() != null && !dto.getProductList().isEmpty()) {
            dto.getProductList().forEach(p -> {
                if (p.getId() == null) {
                    // New product
                    Product product = new Product();
                    product.setId(UUID.randomUUID());
                    product.setName(p.getName());
                    product.setCategory(p.getCategory());
                    product.setPrice(p.getPrice());
                    product.setVendorId(updatedVendor.getId());
                    product.setQuantity_big(p.getQuantity_big());

                    productRepository.save(product);

                    // Add to inventory
                    inventoryClient.addInventory(product.getId().toString(),product.getName(), product.getQuantity_big(), product.getVendorId());
                } else {
                    // Existing product, update quantity in inventory
                    Product existingProduct = productRepository.findById(p.getId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + p.getId()));
                    existingProduct.setName(p.getName() != null ? p.getName() : existingProduct.getName());
                    existingProduct.setPrice(p.getPrice() > 0 ? p.getPrice() : existingProduct.getPrice());
                    existingProduct.setCategory(p.getCategory() != null ? p.getCategory() : existingProduct.getCategory());
                    existingProduct.setQuantity_big(p.getQuantity_big() != null ? p.getQuantity_big() : existingProduct.getQuantity_big());

                    productRepository.save(existingProduct);

                    // Update inventory
                    inventoryClient.updateInventory(existingProduct.getId(), existingProduct.getQuantity_big());
                }
            });
        }

        List<Product> updatedProducts = productRepository.findByVendorId(updatedVendor.getId());
        return toDTO(updatedVendor, updatedProducts);
    }

    // DELETE vendor and its products
    public void deleteVendor(Long id) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));

        List<Product> vendorProducts = productRepository.findByVendorId(vendor.getId());
        if (!vendorProducts.isEmpty()) {
            // Delete from inventory first
            vendorProducts.forEach(p -> inventoryClient.deleteInventory(p.getId()));
            productRepository.deleteAll(vendorProducts);
        }

        vendorRepo.delete(vendor);
    }

    // Helper method to convert entity → DTO
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
