package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;

    // List all products
    public List<ProductOutDTO> getAll() {
        log.info("Fetching all products");
        List<ProductOutDTO> products = productRepository.findAll()
                .stream()
                .map(this::OutDTO)
                .toList();
        log.info("Total products fetched: {}", products.size());
        return products;
    }

    // Get by category
    public List<ProductOutDTO> getByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        List<ProductOutDTO> products = productRepository.findByCategory(category)
                .stream()
                .map(this::OutDTO)
                .toList();
        log.info("Total products in category {}: {}", category, products.size());
        return products;
    }

    // Get by ID
    public ProductDTO getById(UUID id) {
        log.info("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        log.info("Product found: {}", product);
        return toDTO(product);
    }

    // Create product
    public ProductDTO createProduct(ProductDTO dto) {
        log.info("Creating new product: {}", dto);
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setVendorId(dto.getVendorId());
        product.setQuantity_big(dto.getQuantity_big());

        productRepository.save(product);
        log.info("Product created successfully: {}", product);
        return toDTO(product);
    }

    public ProductDTO updateProductPartial(UUID id, ProductDTO dto) {
        log.info("Updating product with ID: {} with data: {}", id, dto);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPrice() > 0) product.setPrice(dto.getPrice());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getQuantity_big() != null) product.setQuantity_big(dto.getQuantity_big());

        productRepository.save(product);
        log.info("Product updated successfully: {}", product);
        return toDTO(product);
    }

    // Delete product
    public void deleteProduct(UUID id) {
        log.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Product not found with ID: {}", id);
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    private ProductOutDTO OutDTO(Product product) {
        ProductOutDTO dto = new ProductOutDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        return dto;
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setVendorId(product.getVendorId());
        dto.setCategory(product.getCategory());
        dto.setQuantity_big(product.getQuantity_big());
        return dto;
    }

    public List<ProductDTO> getList() {
        log.info("Fetching list of all products (ProductDTO)");
        List<Product> list = productRepository.findAll();
        List<ProductDTO> listDto = list.stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setId(product.getId());
                    dto.setVendorId(product.getVendorId());
                    dto.setCategory(product.getCategory());
                    dto.setQuantity_big(product.getQuantity_big());
                    return dto;
                }).toList();
        log.info("Total products fetched: {}", listDto.size());
        return listDto;
    }
}
