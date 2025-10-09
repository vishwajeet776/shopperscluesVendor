package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.InventoryDTO;
import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import com.shopperscluesVendor.shopperscluesVendor.Service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor")
@RequiredArgsConstructor
@Log4j2
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public VendorDTO addVendor(@RequestBody VendorDTO vendor){
        log.info("Received request to add vendor: {}", vendor);
        VendorDTO savedVendor = vendorService.add_vendor(vendor);
        log.info("Vendor added successfully: {}", savedVendor);
        return savedVendor;
    }

    @GetMapping
    public List<Vendor> getAll(){
        log.info("Fetching all vendors");
        List<Vendor> vendors = vendorService.getAll();
        log.info("Total vendors fetched: {}", vendors.size());
        return vendors;
    }

    @GetMapping("/{id}")
    public VendorDTO getVendorById(@PathVariable Long id) {
        log.info("Fetching vendor with ID: {}", id);
        VendorDTO vendor = vendorService.getVendorById(id);
        if (vendor != null) {
            log.info("Vendor found: {}", vendor);
        } else {
            log.warn("Vendor not found with ID: {}", id);
        }
        return vendor;
    }

    @PatchMapping("/{id}")
    public VendorDTO updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        log.info("Updating vendor with ID: {} with data: {}", id, vendorDTO);
        VendorDTO updatedVendor = vendorService.updateVendor(id, vendorDTO);
        log.info("Vendor updated successfully: {}", updatedVendor);
        return updatedVendor;
    }

    @DeleteMapping("/{id}")
    public String deleteVendor(@PathVariable Long id) {
        log.info("Deleting vendor with ID: {}", id);
        vendorService.deleteVendor(id);
        log.info("Vendor deleted successfully with ID: {}", id);
        return "Vendor with ID " + id + " deleted successfully!";
    }
}
