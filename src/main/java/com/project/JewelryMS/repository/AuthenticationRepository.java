package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.aUsername = ?1")
    Account findAccountByUsername(String Username);
    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Account findAccountByemail(String email);

}
