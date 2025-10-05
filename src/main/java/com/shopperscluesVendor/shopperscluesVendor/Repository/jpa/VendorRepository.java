package com.shopperscluesVendor.shopperscluesVendor.Repository.jpa;

import com.shopperscluesVendor.shopperscluesVendor.Entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
