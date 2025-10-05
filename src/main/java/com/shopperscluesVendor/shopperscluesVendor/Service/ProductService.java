package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // List all products
    public List<ProductOutDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::OutDTO)
                .toList();
    }

    // Get by category
    public List<ProductOutDTO> getByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::OutDTO)
                .toList();
    }

    // Get by ID
    public ProductDTO getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        return toDTO(product);
    }

    // Create product
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setVendorId(dto.getVendorId());
        product.setQuantity_big(dto.getQuantity_big());

        productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO updateProductPartial(UUID id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        // Only update fields if they are provided
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getPrice() > 0) { // Assuming price 0 means "not provided"
            product.setPrice(dto.getPrice());
        }
        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }
        if (dto.getQuantity_big() != null) {
            product.setQuantity_big(dto.getQuantity_big());
        }

        productRepository.save(product);
        return toDTO(product);
    }

    // Delete product
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
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
        List<Product> list= productRepository.findAll();
        List<ProductDTO> listDto=list.stream().
                map(product -> {
                    ProductDTO dto=new ProductDTO();
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setId(product.getId());
                    dto.setVendorId(product.getVendorId());
                    dto.setCategory(product.getCategory());
                    dto.setQuantity_big(product.getQuantity_big());
                    return dto;
                }).toList();
        return listDto;
    }
}
