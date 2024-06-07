package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.model.Performance.PerformanceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query("SELECT new com.project.JewelryMS.model.Performance.PerformanceResponse(" +
            "p.PK_performanceID, p.staffAccount.staffID, p.date, p.salesMade, p.revenueGenerated, p.customerSignUp, p.status) " +
            "FROM Performance p WHERE DATE_FORMAT(p.date, '%Y-%m-%d') = :date")
    List<PerformanceResponse> findByDate(@Param("date") String date);


    @Query("SELECT new com.project.JewelryMS.model.Performance.PerformanceResponse(" +
            "p.PK_performanceID, p.staffAccount.staffID, p.date, p.salesMade, p.revenueGenerated, p.customerSignUp, p.status) " +
            "FROM Performance p WHERE p.PK_performanceID = :PK_performanceID")
    Optional<PerformanceResponse> findByPerformanceId(@Param("PK_performanceID") long PK_performanceID);

    @Query("SELECT new com.project.JewelryMS.model.Performance.PerformanceResponse(" +
            "p.PK_performanceID, p.staffAccount.staffID, p.date, p.salesMade, p.revenueGenerated, p.customerSignUp, p.status) " +
            "FROM Performance p WHERE p.status = :status")
    List<PerformanceResponse> findByStatus(@Param("status") boolean status);

    @Query("SELECT new com.project.JewelryMS.model.Performance.PerformanceResponse(" +
            "p.PK_performanceID, p.staffAccount.staffID, p.date, p.salesMade, p.revenueGenerated, p.customerSignUp, p.status) " +
            "FROM Performance p WHERE p.staffAccount.staffID = :staffId")
    Optional<PerformanceResponse> findByStaffId(@Param("staffId") long staffID);

    @Query("SELECT new com.project.JewelryMS.model.Performance.PerformanceResponse(" +
            "p.PK_performanceID, p.staffAccount.staffID, p.date, p.salesMade, p.revenueGenerated, p.customerSignUp, p.status) " +
            "FROM Performance p")
    List<PerformanceResponse> listAll();

}

