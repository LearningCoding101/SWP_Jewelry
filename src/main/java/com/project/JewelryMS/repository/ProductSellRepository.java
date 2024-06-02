package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSellRepository extends JpaRepository<ProductSell, Long> {
    @Query("SELECT new com.project.JewelryMS.model.ProductSell.ProductSellResponse(p.productID, p.carat, p.chi, p.cost, p.pDescription, p.gemstoneType, p.image, p.manufacturer, p.metalType, p.pName, p.productCode, p.productCost, p.pStatus, c.id) " +
            "FROM ProductSell p " +
            "LEFT JOIN p.category c")
    List<ProductSellResponse> findAllProductSellResponses();

}
