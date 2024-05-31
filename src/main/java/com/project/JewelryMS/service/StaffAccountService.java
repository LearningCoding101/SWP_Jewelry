package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.CreateStaffAccountRequest;
import com.project.JewelryMS.model.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.StaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffAccountService {
    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    // Create
    public StaffAccount createStaffAccount(CreateStaffAccountRequest createStaffAccountRequest) {
        StaffAccount newStaffAccount = new StaffAccount();
        newStaffAccount.setPhoneNumber(createStaffAccountRequest.getPhoneNumber());
        newStaffAccount.setSalary(createStaffAccountRequest.getSalary());
        newStaffAccount.setShiftID(createStaffAccountRequest.getShiftID());
        newStaffAccount.setStartDate(createStaffAccountRequest.getStartDate());
        // Directly use the Account from the request
        Account account = createStaffAccountRequest.getAccount();
        if (account != null) {
            newStaffAccount.setAccount(account);
            account.setStaffAccount(newStaffAccount);  // Set the bidirectional relationship
        } else {
            throw new RuntimeException("Account information is missing in the request");
        }
        return staffAccountRepository.save(newStaffAccount);
    }

    // Read all
    public List<StaffAccount> readAllStaffAccounts() {
        return staffAccountRepository.findAll();
    }

    // Read by ID
    public StaffAccount getStaffAccountById(long id) {
        Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(id);
        return staffAccountOptional.orElse(null);
    }


    // Update
    public StaffAccount updateStaffAccount(StaffAccountRequest staffAccountRequest) {
        Optional<StaffAccount> existingStaffAccountOpt = staffAccountRepository.findById(staffAccountRequest.getStaffID());
        if (existingStaffAccountOpt.isPresent()) {
            StaffAccount existingStaffAccount = existingStaffAccountOpt.get();
            existingStaffAccount.setPhoneNumber(staffAccountRequest.getPhoneNumber());
            existingStaffAccount.setSalary(staffAccountRequest.getSalary());
            existingStaffAccount.setShiftID(staffAccountRequest.getShiftID());
            existingStaffAccount.setStartDate(staffAccountRequest.getStartDate());
            existingStaffAccount.setUserID(staffAccountRequest.getUserID());
            return staffAccountRepository.save(existingStaffAccount);
        } else {
            throw new RuntimeException("StaffAccount with ID " + staffAccountRequest.getStaffID() + " not found");
        }
    }

    // Method to "delete" a StaffAccount by updating the Account status
    public void deactivateStaffAccount(DeleteStaffAccountRequest deleteStaffAccountRequest) {
        Optional<StaffAccount> staffAccountOpt = staffAccountRepository.findById(deleteStaffAccountRequest.getStaffID());
        if (staffAccountOpt.isPresent()) {
            StaffAccount staffAccount = staffAccountOpt.get();
            long userID = (long) staffAccount.getUserID();
            Optional<Account> accountOpt = authenticationRepository.findById(userID);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                account.setStatus(false);
                authenticationRepository.save(account);
            } else {
                throw new RuntimeException("Account associated with StaffAccount ID " + deleteStaffAccountRequest.getStaffID() + " not found");
            }
        } else {
            throw new RuntimeException("StaffAccount with ID " + deleteStaffAccountRequest.getStaffID()+ " not found");
        }
    }
}
