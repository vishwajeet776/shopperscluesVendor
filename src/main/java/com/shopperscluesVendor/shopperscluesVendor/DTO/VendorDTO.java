package com.shopperscluesVendor.shopperscluesVendor.DTO;

import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import lombok.Data;

import java.util.List;
@Data
public class VendorDTO {
    private long id;
    private String name;
    private String mailId;
    private String gstNumber;
    private String city;
    private List<ProductDTO> productList;
}
