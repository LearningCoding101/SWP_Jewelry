package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuaranteeRepository extends JpaRepository<Guarantee, Long> {

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g WHERE g.PK_guaranteeID = :PK_guaranteeID")
    Optional<GuaranteeResponse> findByGuaranteeId(@Param("PK_guaranteeID") long PK_guaranteeID);

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g WHERE g.status = :status")
    List<GuaranteeResponse> findByStatus(@Param("status") boolean status);

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g WHERE g.productSell.productID = :productID")
    Optional<GuaranteeResponse> findByProductId(@Param("productID") long productID);

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g WHERE g.policyType LIKE %:policyType%")
    List<GuaranteeResponse> listAllGuaranteesByPolicyType(@Param("policyType") String policyType);

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g WHERE g.coverage LIKE %:coverage%")
    List<GuaranteeResponse> listAllGuaranteesByCoverage(@Param("coverage") String coverage);

    @Query("SELECT new com.project.JewelryMS.model.Guarantee.GuaranteeResponse(" +
            "g.PK_guaranteeID, g.productSell.productID, g.policyType, g.coverage, g.status) " +
            "FROM Guarantee g")
    List<GuaranteeResponse> listAllGuarantees();
}

