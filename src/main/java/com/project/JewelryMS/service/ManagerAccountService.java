package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Manager.CreateManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.model.RegisterRequest;
import com.project.JewelryMS.model.Staff.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerAccountService {

    @Autowired
    ManagerAccountRepository managerAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationRepository authenticationRepository;

    public List<ManagerAccountResponse> getAllManagerAccounts() {
        return managerAccountRepository.findAllManagerAccounts();
    }

    public ManagerAccountResponse getManagerAccountById(int id) {
        return managerAccountRepository.findManagerAccountById(id);
    }

    public ManagerAccountResponse createManagerAccount(CreateManagerAccountRequest createManagerAccountRequest){
    Account account = new Account();
    account.setAccountName(createManagerAccountRequest.getAccountName());
    account.setEmail(createManagerAccountRequest.getEmail());
    account.setAPassword(passwordEncoder.encode(createManagerAccountRequest.getAPassword()));
    account.setAUsername(createManagerAccountRequest.getAUsername());
    account.setRole(RoleEnum.ROLE_MANAGER);
    account.setStatus(1);
    Account account1 =  authenticationRepository.save(account);
    return getManagerAccountById(account1.getPK_userID());
    }

    // Update
    public Account updateManagerAccount(ManagerAccountRequest managerAccountRequest) {
        Optional<Account> existingAccountOpt = managerAccountRepository.findById(managerAccountRequest.getUser_ID());
        if (existingAccountOpt.isPresent()) {
            Account existingStaffAccount = existingAccountOpt.get();
            existingStaffAccount.setAccountName(managerAccountRequest.getAccountName());
            existingStaffAccount.setAUsername(managerAccountRequest.getAUsername());
            existingStaffAccount.setEmail(managerAccountRequest.getEmail());
            return authenticationRepository.save(existingStaffAccount);
        } else {
            throw new RuntimeException("StaffAccount with ID " + managerAccountRequest.getUser_ID() + " not found");
        }
    }

    // Method to "delete" a StaffAccount by updating the Account status
    public void deactivateManagerAccount(long id) {
        Optional<Account> managerAccountOpt = managerAccountRepository.findById(id);
        if (managerAccountOpt.isPresent()) {
            Account account = managerAccountOpt.get();
            account.setStatus(0);
        } else {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }
    }


}


