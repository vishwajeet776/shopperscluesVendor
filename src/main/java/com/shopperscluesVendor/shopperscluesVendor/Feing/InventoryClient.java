package com.shopperscluesVendor.shopperscluesVendor.Feing;

import com.shopperscluesVendor.shopperscluesVendor.DTO.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "SHOPPERSCLUESINVENTORY")
public interface InventoryClient {
    @PostMapping("/inventory/add")
    public InventoryDTO addInventory(@RequestParam String productId,
                                  @RequestParam String name,
                                  @RequestParam Long quantity,
                                  @RequestParam Long vendorId);

    // Update inventory quantity by productId
    @PutMapping("/updateInventory")
    public InventoryDTO updateInventory(@RequestParam UUID productId,
                                     @RequestParam Long quantity);

    @DeleteMapping("/deleteInventory")
    public void deleteInventory(@RequestParam UUID productId);
}
