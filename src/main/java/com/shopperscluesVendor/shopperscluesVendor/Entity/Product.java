package com.shopperscluesVendor.shopperscluesVendor.Entity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("products")
@Data
public class Product {

    @PrimaryKey
    private UUID id;
    @Column("vendorid")// use UUID instead of Long
    private long vendorId;
    private String name;
    private long price;
    private String category;
    @Column("quantity_big")
    private Long quantity_big;
}

