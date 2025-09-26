package com.shopperscluesVendor.shopperscluesVendor.Controller;

import com.shopperscluesVendor.shopperscluesVendor.DTO.VendorDTO;
import com.shopperscluesVendor.shopperscluesVendor.Service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public VendorDTO addVendor(@RequestBody VendorDTO vendor){
        return vendorService.add_vendor(vendor);
    }

}
