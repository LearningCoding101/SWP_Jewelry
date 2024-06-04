package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
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
    public StaffAccountResponse createStaffAccount(CreateStaffAccountRequest createStaffAccountRequest) {
        Optional<Account> accountOptional = authenticationRepository.findById(createStaffAccountRequest.getAccount_id());
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (account.getRole()== RoleEnum.ROLE_STAFF) {
                StaffAccount newStaffAccount = new StaffAccount();
                newStaffAccount.setPhoneNumber(createStaffAccountRequest.getPhoneNumber());
                newStaffAccount.setSalary(createStaffAccountRequest.getSalary());
                newStaffAccount.setShiftID(createStaffAccountRequest.getShiftID());
                newStaffAccount.setStartDate(createStaffAccountRequest.getStartDate());
                newStaffAccount.setAccount(account);
                StaffAccount staffAccount = staffAccountRepository.save(newStaffAccount);
                return getStaffAccountById(staffAccount.getStaffID());
            }else{
                throw new RuntimeException("Account not have Staff Role");
            }
        }else{
            throw new RuntimeException("Account ID not find");
        }
    }

    // Read all
    public List<StaffAccountResponse> readAllStaffAccounts() {
        return staffAccountRepository.findAllStaffAccountsByRoleStaff();
    }

    // Read by ID
    public StaffAccountResponse getStaffAccountById(long id) {
        Optional<StaffAccountResponse> staffAccountOptional = staffAccountRepository.findIDStaffAccount(id);
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
            return staffAccountRepository.save(existingStaffAccount);
        } else {
            throw new RuntimeException("StaffAccount with ID " + staffAccountRequest.getStaffID() + " not found");
        }
    }

    // Method to "delete" a StaffAccount by updating the Account status
    public void deactivateStaffAccount(long id) {
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
