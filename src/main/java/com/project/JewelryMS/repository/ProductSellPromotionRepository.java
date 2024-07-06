package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.ProductSell_Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSellPromotionRepository extends JpaRepository<ProductSell_Promotion, Long> {
    List<ProductSell_Promotion> findByProductSell(ProductSell productSell);
}
