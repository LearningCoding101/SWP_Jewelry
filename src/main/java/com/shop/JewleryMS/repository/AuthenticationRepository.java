package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long> {

    Account findAccountByaUsername(String aUsername);

}
