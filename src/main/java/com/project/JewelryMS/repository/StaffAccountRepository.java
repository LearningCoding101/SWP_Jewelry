package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Long> {
    @Query("SELECT new com.project.JewelryMS.model.Staff.StaffAccountResponse(sa.staffID, sa.shiftID, sa.phoneNumber, sa.salary, sa.startDate, sa.account.email, sa.account.aUsername, sa.account.accountName, sa.account.status) " +
            "FROM StaffAccount sa " +
            "JOIN sa.account acc " +
            "WHERE acc.role = com.project.JewelryMS.entity.RoleEnum.ROLE_STAFF")
    List<StaffAccountResponse> findAllStaffAccountsByRoleStaff();

    @Query("SELECT new com.project.JewelryMS.model.Staff.StaffAccountResponse(sa.staffID, sa.shiftID, sa.phoneNumber, sa.salary, sa.startDate, sa.account.email, sa.account.aUsername, sa.account.accountName, sa.account.status) " +
            "FROM StaffAccount sa " +
            "JOIN sa.account acc " +
            "WHERE acc.role = com.project.JewelryMS.entity.RoleEnum.ROLE_STAFF AND sa.staffID = :id")
    Optional<StaffAccountResponse> findIDStaffAccount(long id);
}
