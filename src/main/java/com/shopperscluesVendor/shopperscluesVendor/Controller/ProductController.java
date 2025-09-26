package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductDTO> getAll(){
       return productService.get_all();
    }

    @GetMapping("/random")
    public List<ProductDTO> getRandom(@RequestParam (defaultValue = "5") int count){
        List<ProductDTO> list = new ArrayList<>(productService.get_all()); // mutable copy
        Collections.shuffle(list);
        return list.subList(0, count);

    }


}
