package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByStatus(boolean b);
    Optional<Customer> findByPhoneNumber(String n);
}