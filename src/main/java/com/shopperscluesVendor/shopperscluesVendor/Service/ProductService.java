package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductOutDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductOutDTO> get_all() {
        List<Product> productList= productRepository.findAll();
        List<ProductOutDTO> productDTOList=productList.stream().
                map(product -> {
                    ProductOutDTO pDTO=new ProductOutDTO();
                    pDTO.setId(product.getId());
                    pDTO.setName(product.getName());
                    pDTO.setCategory(product.getCategory());
                    pDTO.setPrice(product.getPrice());
                    return pDTO;
                }).toList();
        return productDTOList;

    }

    public List<ProductOutDTO> getByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(product -> {
                    ProductOutDTO dto = new ProductOutDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setCategory(product.getCategory());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .toList();
    }

}
