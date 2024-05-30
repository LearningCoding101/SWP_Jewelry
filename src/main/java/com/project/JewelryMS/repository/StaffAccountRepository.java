package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Long> {
}
