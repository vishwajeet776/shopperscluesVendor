package com.shopperscluesVendor.shopperscluesVendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.shopperscluesVendor.shopperscluesVendor.Repository")
public class ShopperscluesVendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopperscluesVendorApplication.class, args);
		System.out.println("spring boot Started ......");
	}

}
