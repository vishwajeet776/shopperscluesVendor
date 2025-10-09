package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductOutDTO> getAll() {
        log.info("Fetching all products (ProductOutDTO)");
        List<ProductOutDTO> products = productService.getAll();
        log.info("Total products fetched: {}", products.size());
        return products;
    }

    @GetMapping("/getAll")
    public List<ProductDTO> List() {
        log.info("Fetching all products (ProductDTO)");
        List<ProductDTO> products = productService.getList();
        log.info("Total products fetched: {}", products.size());
        return products;
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable UUID id) {
        log.info("Fetching product by ID: {}", id);
        ProductDTO product = productService.getById(id);
        log.info("Product fetched: {}", product);
        return product;
    }

    @GetMapping("/category/{category}")
    public List<ProductOutDTO> getByCategory(@PathVariable String category) {
        log.info("Fetching products by category: {}", category);
        List<ProductOutDTO> products = productService.getByCategory(category);
        log.info("Total products fetched in category {}: {}", category, products.size());
        return products;
    }

    @PostMapping("/add")
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        log.info("Creating new product: {}", dto);
        ProductDTO created = productService.createProduct(dto);
        log.info("Product created successfully: {}", created);
        return created;
    }

    @PatchMapping("/update/{id}")
    public ProductDTO updateProduct(@PathVariable UUID id, @RequestBody ProductDTO dto) {
        log.info("Updating product with ID: {} with data: {}", id, dto);
        ProductDTO updated = productService.updateProductPartial(id, dto);
        log.info("Product updated successfully: {}", updated);
        return updated;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        log.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Product deleted successfully with ID: {}", id);
        return "Product deleted successfully";
    }
}
