package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    // Create

    // Read all
    public List<StaffAccountResponse> readAllStaffAccounts() {
        return staffAccountRepository.findAllStaffAccountsByRoleStaff();
    }

    // Read by ID
    public StaffAccountResponse getStaffAccountById(Integer id) {
        Optional<StaffAccountResponse> staffAccountOptional = staffAccountRepository.findIDStaffAccount(id);
        return staffAccountOptional.orElse(null);
    }


    // Update
    @Transactional
    public StaffAccountResponse updateStaffAccount(Integer id, StaffAccountRequest staffAccountRequest) {
        Optional<StaffAccount> existingStaffAccountOpt = staffAccountRepository.findById(id);
        if (!existingStaffAccountOpt.isPresent()) {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }

        StaffAccount existingStaffAccount = existingStaffAccountOpt.get();

        // Update fields from StaffAccountRequest
        existingStaffAccount.setPhoneNumber(staffAccountRequest.getPhoneNumber());
        existingStaffAccount.setSalary(staffAccountRequest.getSalary());
        existingStaffAccount.setStartDate(staffAccountRequest.getStartDate());

        // Update account information
        Optional<Account> accountOpt = authenticationRepository.findById((long) existingStaffAccount.getAccount().getPK_userID());
        if (!accountOpt.isPresent()) {
            throw new RuntimeException("Account with ID " + existingStaffAccount.getAccount().getPK_userID() + " not found");
        }
        Account account = accountOpt.get();
        account.setEmail(staffAccountRequest.getEmail());
        account.setAUsername(staffAccountRequest.getUsername());
        account.setAPassword(passwordEncoder.encode(staffAccountRequest.getPassword()));
        account.setAccountName(staffAccountRequest.getAccountName());
        account.setRole(staffAccountRequest.getRole()); // Update role from request

        // Save the updated entities
        authenticationRepository.save(account);
        staffAccountRepository.save(existingStaffAccount);

        // Prepare and return the response
        return new StaffAccountResponse(
                existingStaffAccount.getStaffID(),
                existingStaffAccount.getPhoneNumber(),
                existingStaffAccount.getSalary(),
                existingStaffAccount.getStartDate(),
                account.getEmail(),
                account.getAUsername(),
                account.getAccountName(),
                account.getRole(),
                account.getStatus(),
                existingStaffAccount.getShift()
//                existingStaffAccount.getShift().getStartTime(), // Assuming start time is unchanged
//                existingStaffAccount.getShift().getRegister(), // Assuming register is unchanged
//                existingStaffAccount.getShift().getEndTime(), // Assuming end time is unchanged
//                existingStaffAccount.getShift().getShiftType(), // Assuming shift type is unchanged
//                existingStaffAccount.getShift().getStatus(), // Assuming status is unchanged
//                existingStaffAccount.getShift().getWorkArea() // Assuming work area is unchanged
        );
    }

    // Method to "delete" a StaffAccount by updating the Account status
    public void deactivateStaffAccount(Integer id) {
        Optional<StaffAccount> staffAccountOpt = staffAccountRepository.findById(id);
        if (staffAccountOpt.isPresent()) {
            StaffAccount staffAccount = staffAccountOpt.get();
            Account account = staffAccount.getAccount();
            if (account != null) {
                account.setStatus(0);
                authenticationRepository.save(account);
            } else {
                throw new RuntimeException("Account associated with StaffAccount ID " + id + " not found");
            }
        } else {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }
    }
}
