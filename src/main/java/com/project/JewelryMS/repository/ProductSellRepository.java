package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSellRepository extends JpaRepository<ProductSell, Long> {
}
