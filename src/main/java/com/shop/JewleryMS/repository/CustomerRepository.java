package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}