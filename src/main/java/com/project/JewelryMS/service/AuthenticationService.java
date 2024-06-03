package com.project.JewelryMS.service;


import com.beust.ah.A;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        account.setStatus(1);
        return authenticationRepository.save(account);
    }
    public Boolean handleRegisterCheckEmail(RegisterRequest registerRequest){
        String emailCheck = registerRequest.getEmail();
        if(emailService.validEmail(emailCheck)){
            return true;
        }
        return false;
    }


    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }

    public Account getAccount(String email){
        return authenticationRepository.findAccountByemail(email);
    }
    public Account getAccountById(Long id){
        return authenticationRepository.findAccountById(id);
    }
    public String sendForgotPasswordEmail(String email) {
        Optional<Account> accountOptional = Optional.ofNullable(authenticationRepository.findAccountByemail(email.replace("\"", "")));
        if (accountOptional.isPresent()) {

            Account account = accountOptional.get();
            String tempPassword = emailService.sendTempPassword(account);
            account.setAPassword(passwordEncoder.encode(tempPassword));
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
                    accountResponse.setId((long)account.getPK_userID());
                    return accountResponse;
    }

    public void changePassword(){}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return authenticationRepository.findAccountByUsername(username);
    }


    public List<Account> getAllManagerAccount() {
        return authenticationRepository.findByRole(RoleEnum.ROLE_MANAGER);
    }


    public AccountResponseGG loginGoogle(LoginGoogleRequest loginGoogleRequest) {
        AccountResponseGG accountResponseGG = new AccountResponseGG();
        try{
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(loginGoogleRequest.getToken());
            String email = firebaseToken.getEmail();
            Account account = authenticationRepository.findAccountByemail(email);
            if(account == null){
                 account = new Account();
                 account.setEmail(email);
                 account.setAUsername(firebaseToken.getName());
                 account.setRole(RoleEnum.ROLE_ADMIN);
                 account = authenticationRepository.save(account);
            }
             accountResponseGG.setEmail(account.getEmail());
             accountResponseGG.setAUsername(account.getAUsername());
             accountResponseGG.setPK_userID(account.getPK_userID());
             String token = jwTservice.generateToken(account);
             accountResponseGG.setToken(token);
             accountResponseGG.setRole(account.getRole());

        }catch(FirebaseAuthException e){
            e.printStackTrace();
        }
        return accountResponseGG;
    }
}
