package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Integer> {

        @Query("SELECT sa FROM StaffAccount sa JOIN sa.account a WHERE a.role = 'ROLE_STAFF' AND a.status = 1")
        List<StaffAccount> findAllStaffAccountsByRoleStaff();

        @Query("SELECT sa FROM StaffAccount sa JOIN sa.account a WHERE a.role = 'ROLE_STAFF' AND a.status = 1 AND sa.staffID = :id")
        Optional<StaffAccount> findIDStaffAccount(@Param("id") Integer id);
}
