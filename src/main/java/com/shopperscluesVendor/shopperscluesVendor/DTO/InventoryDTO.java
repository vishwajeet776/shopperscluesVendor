package com.shopperscluesVendor.shopperscluesVendor.DTO;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class InventoryDTO {

    private String productId;
    private String name;
    private Long quantity;
    private Long vendorId;
    private Instant lastUpdated;
}
