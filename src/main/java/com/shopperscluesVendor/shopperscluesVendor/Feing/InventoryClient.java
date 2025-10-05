package com.shopperscluesVendor.shopperscluesVendor.Feing;

import com.shopperscluesVendor.shopperscluesVendor.DTO.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SHOPPERSCLUESINVENTORY")
public interface InventoryClient {
    @PostMapping("/inventory/add")
    public InventoryDTO addInventory(@RequestParam String name,
                                     @RequestParam Long quantity,
                                     @RequestParam Long vendorId);
}
