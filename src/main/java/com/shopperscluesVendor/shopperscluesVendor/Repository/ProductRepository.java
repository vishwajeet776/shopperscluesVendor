package com.shopperscluesVendor.shopperscluesVendor.Repository;

import com.shopperscluesVendor.shopperscluesVendor.DTO.ProductDTO;
import com.shopperscluesVendor.shopperscluesVendor.Entity.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {
   @Query("SELECT * FROM products WHERE category = ?0 ALLOW FILTERING")
   List<Product> findByCategory(String category);

//   public List<Product> findAllByCategory(String Category);
}
