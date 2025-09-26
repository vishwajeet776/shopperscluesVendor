package com.shopperscluesVendor.shopperscluesVendor.Repository;

import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {
}
