package com.shopperscluesVendor.shopperscluesVendor.Repository.cassandra;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {

   List<Product> findByCategory(String category);

   List<Product> findByVendorId(long id);

//   public List<Product> findAllByCategory(String Category);
}
