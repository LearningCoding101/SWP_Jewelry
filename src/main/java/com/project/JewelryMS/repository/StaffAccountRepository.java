package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Integer> {
    @Query("SELECT new com.project.JewelryMS.model.Staff.StaffAccountResponse(sa.staffID, sa.phoneNumber, sa.salary, sa.startDate, acc.email, acc.aUsername, acc.accountName, acc.role, acc.status, sh.shiftID, sh.startTime, sh.register, sh.endTime, sh.shiftType, sh.status, sh.workArea) " +
            "FROM Shift sh " +
            "JOIN sh.staffAccounts sa " +
            "JOIN sa.account acc " +
            "WHERE acc.role = com.project.JewelryMS.entity.RoleEnum.ROLE_STAFF AND acc.status = 1")
    List<StaffAccountResponse> findAllStaffAccountsByRoleStaff();

    @Query("SELECT new com.project.JewelryMS.model.Staff.StaffAccountResponse(sa.staffID, sa.phoneNumber, sa.salary, sa.startDate, acc.email, acc.aUsername, acc.accountName, acc.role, acc.status, sh.shiftID, sh.startTime, sh.register, sh.endTime, sh.shiftType, sh.status, sh.workArea) " +
            "FROM Shift sh " +
            "JOIN sh.staffAccounts sa " +
            "JOIN sa.account acc " +
            "WHERE acc.role = com.project.JewelryMS.entity.RoleEnum.ROLE_STAFF AND acc.status = 1 AND sa.staffID = :id")
    Optional<StaffAccountResponse> findIDStaffAccount(Integer id);
}
