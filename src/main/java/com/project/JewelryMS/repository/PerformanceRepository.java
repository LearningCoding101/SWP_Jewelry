package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query("SELECT p FROM Performance p WHERE DATE_FORMAT(p.date, '%Y-%m-%d') = :date")
    List<Performance> findByDate(@Param("date") String date);

    @Query("SELECT p FROM Performance p WHERE p.PK_performanceID = :PK_performanceID")
    Performance findByPerformanceId(@Param("PK_performanceID") long PK_performanceID);

    @Query("SELECT p FROM Performance p WHERE p.status = :status")
    List<Performance> findByStatus(@Param("status") boolean status);

    @Query("SELECT p FROM Performance p WHERE p.staffAccount.staffID = :staffId")
    Performance findByStaffId(@Param("staffId") long staffID);

    @Query("SELECT p FROM Performance p")
    List<Performance> listAll();
}

