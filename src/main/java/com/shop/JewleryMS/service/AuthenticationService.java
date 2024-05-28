package com.shop.JewleryMS.service;

import com.shop.JewleryMS.config.SecurityConfig;
import com.shop.JewleryMS.entity.Account;
import com.shop.JewleryMS.entity.RoleEnum;
import com.shop.JewleryMS.model.AccountResponse;
import com.shop.JewleryMS.model.LoginRequest;
import com.shop.JewleryMS.model.RegisterRequest;
import com.shop.JewleryMS.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    EmailService emailService;
    @Autowired
    JWTservice jwTservice;

    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    public Account register(RegisterRequest registerRequest){
        Account account = new Account();
        account.setAccountName(registerRequest.getAccountName());
        account.setEmail(registerRequest.getEmail());
        account.setAPassword(passwordEncoder.encode(registerRequest.getAPassword()));
        account.setAUsername(registerRequest.getAUsername());
        account.setRole(RoleEnum.ROLE_ADMIN);
        return authenticationRepository.save(account);
    }
    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }

    public Account getAccount(String email){
        return authenticationRepository.findAccountByemail(email);
    }
    public String sendForgotPasswordEmail(String email) {
        Optional<Account> accountOptional = Optional.ofNullable(authenticationRepository.findAccountByemail(email));
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            System.out.println(account.getAccountName());
            String tempPassword = emailService.sendTempPassword(account);
            account.setAPassword(tempPassword);
            authenticationRepository.save(account);
            return "Successfully sent new temporary password";
        } else {
            return "Is your email correct?";
        }
    }
    public AccountResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
                Account account = authenticationRepository.findAccountByUsername(loginRequest.getUsername());
        String token = jwTservice.generateToken(account);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setUsername(account.getUsername());
        accountResponse.setToken(token);
        accountResponse.setRole(account.getRole());
        return accountResponse;
    }

    public void changePassword(){}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return authenticationRepository.findAccountByUsername(username);
    }
}
