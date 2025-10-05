package com.shopperscluesVendor.shopperscluesVendor.DTO;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class InventoryDTO {
    @Id
    private UUID productId;   // Primary key in MongoDB
    private String name;
    private Long quantity;
    private Long vendorId;
    private Instant lastUpdated;
}
