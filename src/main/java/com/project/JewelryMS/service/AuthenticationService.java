package com.project.JewelryMS.service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.enumClass.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.exception.DuplicateEmailException;
import com.project.JewelryMS.exception.DuplicateUsernameException;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    EmailService emailService;
    @Autowired
    JWTservice jwTservice;

    @Autowired
    StaffShiftRepository staffShiftRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    StaffAccountRepository staffAccountRepository;

    public CreateStaffAccountRequest register(CreateStaffAccountRequest createStaffAccountRequest){
        Account account = new Account();
        if(authenticationRepository.existsByAUsername(createStaffAccountRequest.getUsername())){
            throw new DuplicateUsernameException("Username đã được sử dụng.");
        }
        if(authenticationRepository.existsByEmail(createStaffAccountRequest.getUsername())){
            throw new DuplicateEmailException("Email đã được sử dụng.");
        }
        account.setAccountName(createStaffAccountRequest.getAccountName());

        account.setEmail(createStaffAccountRequest.getEmail());
        account.setAPassword(passwordEncoder.encode(createStaffAccountRequest.getPassword()));
        account.setAUsername(createStaffAccountRequest.getUsername());
        account.setRole(RoleEnum.ROLE_STAFF);
        account.setAImage("https://i.ibb.co/Gxz9md6/avatar-trang-4.jpg");
        StaffAccount staffAccount = new StaffAccount();
        staffAccount.setSalary(createStaffAccountRequest.getSalary());
        staffAccount.setPhoneNumber(createStaffAccountRequest.getPhoneNumber());
        staffAccount.setStartDate(LocalDate.now());
        staffAccount.setAccount(account);
        account.setStatus(1);
        staffAccountRepository.save(staffAccount);
        authenticationRepository.save(account);
        return createStaffAccountRequest;
    }
    public Boolean handleRegisterCheckEmail(CreateStaffAccountRequest registerRequest){
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

                    // Update attendance status if the login time falls within any active shift
                    updateAttendanceStatusOnLogin(account, LocalDateTime.now());
                    return accountResponse;
    }
    public ResponseEntity<String> changePassword(ChangePasswordRequest request ,Long id) {
        Account account = authenticationRepository.findById(id).orElse(null);

        if (account == null) {
            return new ResponseEntity<>("Tài khoản không được tìm thấy", HttpStatus.NOT_FOUND);
        }

        if(request.getNewPassword().equals(request.getOldPassword())){
            return new ResponseEntity<>("mật khẩu mới không được trùng với mật khẩu cũ ", HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), account.getAPassword())) {
            return new ResponseEntity<>("mặt khẩu cũ không khớp", HttpStatus.BAD_REQUEST);
        }

        account.setAPassword(passwordEncoder.encode(request.getNewPassword()));
        authenticationRepository.save(account);

        return new ResponseEntity<>("Đã thay đổi mật khẩu thành công", HttpStatus.OK);
    }
@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = authenticationRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account;
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

            // Update attendance status if the login time falls within any active shift
            updateAttendanceStatusOnLogin(account, LocalDateTime.now());
        }catch(FirebaseAuthException e){
            e.printStackTrace();
        }
        return accountResponseGG;
    }

    private void updateAttendanceStatusOnLogin(Account account, LocalDateTime loginTime) {
        if (account.getRole() != RoleEnum.ROLE_STAFF) {
            return;
        }

        List<Staff_Shift> activeShifts = staffShiftRepository.findActiveShiftsForStaff((long) account.getPK_userID(), loginTime);

        for (Staff_Shift staffShift : activeShifts) {
            if ("Not yet".equals(staffShift.getAttendanceStatus())) {
                // Check if the login time is within 30 minutes of the shift start time
                if (!loginTime.isBefore(staffShift.getShift().getStartTime().minusMinutes(30)) &&
                        !loginTime.isAfter(staffShift.getShift().getStartTime().plusMinutes(30))) {
                    staffShift.setAttendanceStatus("Attended");
                } else {
                    staffShift.setAttendanceStatus("Absent");
                }
                staffShiftRepository.save(staffShift);
            }
        }
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void updateAllStaffAttendance() {
        List<Account> staffAccounts = authenticationRepository.findByRole(RoleEnum.ROLE_STAFF);
        LocalDateTime now = LocalDateTime.now();

        for (Account staffAccount : staffAccounts) {
            List<Staff_Shift> activeShifts = staffShiftRepository.findActiveShiftsForStaff((long) staffAccount.getPK_userID(), now);

            for (Staff_Shift staffShift : activeShifts) {
                if ("Not yet".equals(staffShift.getAttendanceStatus())) {
                    // Check if the current time is more than 120 minutes past the shift start time
                    if (now.isAfter(staffShift.getShift().getStartTime().plusMinutes(120))) {
                        staffShift.setAttendanceStatus("Absent");
                        staffShiftRepository.save(staffShift);
                    }
                }
            }
        }
    }
}
