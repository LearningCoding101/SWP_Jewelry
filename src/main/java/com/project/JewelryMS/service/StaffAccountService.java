package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.exception.DuplicateEmailException;
import com.project.JewelryMS.exception.DuplicateUsernameException;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.model.Staff.StaffAccountWithoutShiftResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class StaffAccountService {
    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Read all
    public List<StaffAccountResponse> readAllStaffAccounts() {
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllStaffAccountsByRoleStaff();
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    private StaffAccountResponse mapToStaffAccountResponse(StaffAccount staffAccount) {
        StaffAccountResponse response = new StaffAccountResponse();
        response.setStaffID(staffAccount.getStaffID());
        response.setPhoneNumber(staffAccount.getPhoneNumber());
        response.setSalary(staffAccount.getSalary());
        response.setStartDate(staffAccount.getStartDate());
        response.setAccountName(staffAccount.getAccount().getAccountName());
        response.setImage(staffAccount.getAccount().getAImage());
        response.setRole(staffAccount.getAccount().getRole());
        response.setStatus(staffAccount.getAccount().getStatus());
        response.setEmail(staffAccount.getAccount().getEmail());
        response.setUsername(staffAccount.getAccount().getUsername());
        if (staffAccount.getStaffShifts() != null && !staffAccount.getStaffShifts().isEmpty()) {
            List<StaffAccountResponse.ShiftResponse> shifts = staffAccount.getStaffShifts().stream()
                    .map(staffShift -> mapToShiftResponse(staffShift.getShift()))
                    .collect(Collectors.toList());
            response.setShift(shifts);
        } else {
            response.setShift(Collections.emptyList());
        }

        return response;
    }

    private StaffAccountResponse.ShiftResponse mapToShiftResponse(Shift shift) {
        StaffAccountResponse.ShiftResponse shiftResponse = new StaffAccountResponse.ShiftResponse();
        shiftResponse.setShiftID(shift.getShiftID());
        shiftResponse.setEndTime(shift.getEndTime());
        shiftResponse.setShiftType(shift.getShiftType());
        shiftResponse.setStartTime(shift.getStartTime());
        shiftResponse.setStatus(shift.getStatus());
        return shiftResponse;
    }


    // Read by ID
    public StaffAccountResponse getStaffAccountById(Integer id) {
        Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findIDStaffAccount(id);
        return staffAccountOptional.map(this::mapToStaffAccountResponse).orElse(null);
    }

    // Update
    @Transactional
    public String updateStaffAccount(Integer id, StaffAccountRequest staffAccountRequest) {
        Optional<StaffAccount> existingStaffAccountOpt = staffAccountRepository.findById(id);
        if (existingStaffAccountOpt.isEmpty()) {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }

        StaffAccount existingStaffAccount = existingStaffAccountOpt.get();

        // Update fields from StaffAccountRequest
        existingStaffAccount.setPhoneNumber(staffAccountRequest.getPhoneNumber());
        existingStaffAccount.setSalary(staffAccountRequest.getSalary());
        existingStaffAccount.setStartDate(staffAccountRequest.getStartDate());

        // Update account information
        Optional<Account> accountOpt = authenticationRepository.findById((long) existingStaffAccount.getAccount().getPK_userID());
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account with ID " + existingStaffAccount.getAccount().getPK_userID() + " not found");
        }
        Account account = accountOpt.get();
        if (authenticationRepository.existsByAUsernameAndPkUserIDNot(account.getAUsername(), account.getPK_userID())) {
            throw new DuplicateUsernameException("Username đã được sử dụng.");
        }
        if (authenticationRepository.existsByEmailAndPkUserIDNot(account.getEmail(), account.getPK_userID())) {
            throw new DuplicateEmailException("Email đã được sử dụng.");
        }
        account.setEmail(staffAccountRequest.getEmail());
        account.setAUsername(staffAccountRequest.getUsername());
//        account.setAPassword(passwordEncoder.encode(staffAccountRequest.getPassword()));
        account.setAccountName(staffAccountRequest.getAccountName());
        account.setRole(staffAccountRequest.getRole()); // Update role from request

        // Save the updated entities
        authenticationRepository.save(account);
        staffAccountRepository.save(existingStaffAccount);

        // Prepare and return the response
        return "Update Staff Successfully";
    }

    // Method to "deactivate" a StaffAccount
    public void deactivateStaffAccount(Integer id) {
        Optional<StaffAccount> staffAccountOpt = staffAccountRepository.findById(id);
        if (staffAccountOpt.isPresent()) {
            StaffAccount staffAccount = staffAccountOpt.get();
            Account account = staffAccount.getAccount();
            if (account != null) {
                account.setStatus(0); // Deactivate the account
                staffAccount.setWorkArea(null); // Set the work area to null
                authenticationRepository.save(account);
                staffAccountRepository.save(staffAccount);
            } else {
                throw new RuntimeException("Account associated with StaffAccount ID " + id + " not found");
            }
        } else {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }
    }

    public List<StaffAccountResponse> getStaffWithoutShift() {
        List<StaffAccount> staffWithoutShift = staffAccountRepository.findStaffWithoutShift();
        return staffWithoutShift.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    // Method to read all staff accounts without their schedules
    public List<StaffAccountWithoutShiftResponse> readAllStaffAccountsWithoutShift() {
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllStaffAccountsByRoleStaff();
        return staffAccounts.stream()
                .map(this::mapToStaffAccountWithoutShiftResponse)
                .collect(Collectors.toList());
    }

    private StaffAccountWithoutShiftResponse mapToStaffAccountWithoutShiftResponse(StaffAccount staffAccount) {
        StaffAccountWithoutShiftResponse response = new StaffAccountWithoutShiftResponse();
        response.setStaffID(staffAccount.getStaffID());
        response.setPhoneNumber(staffAccount.getPhoneNumber());
        response.setSalary(staffAccount.getSalary());
        response.setStartDate(staffAccount.getStartDate());
        response.setAccountName(staffAccount.getAccount().getAccountName());
        response.setImage(staffAccount.getAccount().getAImage());
        response.setRole(staffAccount.getAccount().getRole());
        response.setStatus(staffAccount.getAccount().getStatus());
        response.setEmail(staffAccount.getAccount().getEmail());
        response.setUsername(staffAccount.getAccount().getUsername());
        return response;
    }
}