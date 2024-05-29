package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.entity.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Integer> {


}
