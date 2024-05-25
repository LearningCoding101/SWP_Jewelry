package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.Account;
import com.shop.JewleryMS.Model.RegisterRequest;
import com.shop.JewleryMS.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public Account register(RegisterRequest registerRequest){
        Account account = new Account();
        account.setAccountName(registerRequest.getAccountName());
        account.setEmail(registerRequest.getEmail());
        account.setAPassword(passwordEncoder.encode(registerRequest.getAPassword()));
        account.setAUsername(registerRequest.getAUsername());
        return authenticationRepository.save(account);
    }
    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return authenticationRepository.findAccountByaUsername(username);
    }
}
