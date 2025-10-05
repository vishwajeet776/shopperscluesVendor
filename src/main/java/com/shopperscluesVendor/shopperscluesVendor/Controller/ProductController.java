package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductOutDTO> getAll() {
        return productService.getAll();
    }
    @GetMapping("/getAll")
    public List<ProductDTO> List() {
        return productService.getList();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    @GetMapping("/category/{category}")
    public List<ProductOutDTO> getByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }

    @PostMapping("/add")
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }


    @PatchMapping("/update/{id}")
    public ProductDTO updateProduct(@PathVariable UUID id, @RequestBody ProductDTO dto) {
        return productService.updateProductPartial(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}
