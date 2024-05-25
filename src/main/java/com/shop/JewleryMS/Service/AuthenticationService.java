package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.Account;
import com.shop.JewleryMS.Model.RegisterRequest;
import com.shop.JewleryMS.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    AuthenticationRepository authenticationRepository;

    public Account register(RegisterRequest registerRequest){
        Account account = new Account();
        account.setAccountName(registerRequest.getAccountName());
        account.setEmail(registerRequest.getEmail());
        account.setAPassword(registerRequest.getAPassword());
        account.setAUsername(registerRequest.getAUsername());
        return authenticationRepository.save(account);
    }
    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }
}
