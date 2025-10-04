package com.shopperscluesVendor.shopperscluesVendor.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductOutDTO {

        private UUID id;
        private String name;
        private long price;
        private String category;

}

