package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDTO> get_all() {
        List<Product> productList= productRepository.findAll();
        List<ProductDTO> productDTOList=productList.stream().
                map(product -> {
                    ProductDTO pDTO=new ProductDTO();
                    pDTO.setId(product.getId());
                    pDTO.setName(product.getName());
                    pDTO.setCategory(product.getCategory());
                    pDTO.setPrice(product.getPrice());
                    pDTO.setVendorId(product.getVendorId());
                    return pDTO;
                }).toList();
        return productDTOList;

    }
}
