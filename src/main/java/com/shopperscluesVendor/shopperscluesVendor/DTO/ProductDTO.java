package com.shopperscluesVendor.shopperscluesVendor.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;
    private String name;
    private long price;
    private long vendorId;
    private String category;
}
