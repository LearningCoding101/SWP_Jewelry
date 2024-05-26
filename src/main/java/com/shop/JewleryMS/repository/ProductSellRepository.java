package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.Entity.ProductSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSellRepository extends JpaRepository<ProductSell, Integer> {
}
