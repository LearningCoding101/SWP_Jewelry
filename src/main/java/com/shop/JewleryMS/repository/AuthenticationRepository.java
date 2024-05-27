package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long> {

    Account findAccountByaUsername(String aUsername);

}
