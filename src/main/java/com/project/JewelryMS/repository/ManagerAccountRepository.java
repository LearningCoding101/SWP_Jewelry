package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerAccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByRole(RoleEnum role);
}
