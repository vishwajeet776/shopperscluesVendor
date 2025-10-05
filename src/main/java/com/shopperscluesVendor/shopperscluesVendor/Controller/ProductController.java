package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductOutDTO> getAll(){
       return productService.get_all();
    }

    @GetMapping("/random")
    public List<ProductOutDTO> getRandom(@RequestParam (defaultValue = "5") int count){
        List<ProductOutDTO> list = new ArrayList<>(productService.get_all()); // mutable copy
        Collections.shuffle(list);
        return list.subList(0, count);
    }

    @GetMapping("/category/{category}")
    public List<ProductOutDTO> getByCategory(@PathVariable String category){
        return productService.getByCategory(category);
    }



}
