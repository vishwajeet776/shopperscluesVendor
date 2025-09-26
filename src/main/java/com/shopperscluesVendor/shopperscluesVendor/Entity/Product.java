package com.shopperscluesVendor.shopperscluesVendor.Entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("products")
@Data
public class Product {
    @PrimaryKey
    private UUID id;   // use UUID instead of Long

    private long vendorId;
    private String name;
    private long price;
    private String category;
}

