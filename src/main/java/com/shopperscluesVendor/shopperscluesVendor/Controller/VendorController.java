package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import com.shopperscluesVendor.shopperscluesVendor.Service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public VendorDTO addVendor(@RequestBody VendorDTO vendor){
        return vendorService.add_vendor(vendor);
    }

    @GetMapping
    public List<Vendor> getAll(){
        return vendorService.getAll();
    }

    // READ one vendor
    @GetMapping("/{id}")
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    // UPDATE vendor (partial update)
    @PatchMapping("/{id}")
    public VendorDTO updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.updateVendor(id, vendorDTO);
    }

    // DELETE vendor
    @DeleteMapping("/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return "Vendor with ID " + id + " deleted successfully!";
    }
}
