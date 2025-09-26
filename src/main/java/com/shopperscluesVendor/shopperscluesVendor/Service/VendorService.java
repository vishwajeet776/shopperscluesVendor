package com.shopperscluesVendor.shopperscluesVendor.Service;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import com.shopperscluesVendor.shopperscluesVendor.Repository.ProductRepository;
import com.shopperscluesVendor.shopperscluesVendor.Repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepo;
    private final ProductRepository productRepository;

    public VendorDTO add_vendor(VendorDTO vendorDTO){
        Vendor vendor=new Vendor();

        vendor.setName(vendorDTO.getName());
        vendor.setCity(vendorDTO.getCity());
        vendor.setGstNumber(vendorDTO.getGstNumber());

        Vendor savedVendor=vendorRepo.save(vendor);

        List<Product> productlist =vendorDTO.getProductList().stream()
                .map(dto->{
                    Product product=new Product();
                    product.setId(UUID.randomUUID());
                    product.setName(dto.getName());
                    product.setCategory(dto.getCategory());
                    product.setPrice(dto.getPrice());
                    product.setVendorId(savedVendor.getId());
                    return product;
                }).toList();

         productRepository.saveAll(productlist);


        VendorDTO returnDTO =new VendorDTO();

        returnDTO.setId(savedVendor.getId());
        returnDTO.setName(savedVendor.getName());
        returnDTO.setCity(savedVendor.getCity());
        returnDTO.setGstNumber(savedVendor.getGstNumber());

        List<ProductDTO> productDTOs = productlist.stream()
                .map(p -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setName(p.getName());
                    dto.setPrice(p.getPrice());
                    dto.setCategory(p.getCategory());
                    dto.setVendorId(p.getVendorId());
                    return dto;
                })
                .toList();

        returnDTO.setProductList(productDTOs);

        return returnDTO;



    }
}
