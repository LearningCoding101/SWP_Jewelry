package com.shop.JewleryMS.service;

import com.shop.JewleryMS.entity.StaffAccount;
import com.shop.JewleryMS.model.CreateStaffAccountRequest;
import com.shop.JewleryMS.model.StaffAccountRequest;
import com.shop.JewleryMS.repository.StaffAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffAccountService {
    private static final Logger logger = LoggerFactory.getLogger(StaffAccountService.class);
    @Autowired
    StaffAccountRepository staffAccountRepository;

    public StaffAccount CreateStaffAccount(CreateStaffAccountRequest createStaffAccountRequest){
        StaffAccount account = new StaffAccount();
        account.setUserID(createStaffAccountRequest.getUserID());
        account.setShiftID(createStaffAccountRequest.getShiftID());
        account.setPhoneNumber(createStaffAccountRequest.getPhoneNumber());
        account.setStartDate(createStaffAccountRequest.getStartDate());
        account.setSalary(createStaffAccountRequest.getSalary());
        return staffAccountRepository.save(account);
    }

    public List<StaffAccount> ReadStaffAccounts(){
        return staffAccountRepository.findAll();
    }

    public void UpdateStaffAccount(StaffAccountRequest staffAccountRequest){
        Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(staffAccountRequest.getStaffID());
        if(staffAccountOptional.isPresent()){
            StaffAccount staffAccount = staffAccountOptional.get();
            staffAccount.setStaffID(staffAccountRequest.getStaffID());
            staffAccount.setUserID(staffAccountRequest.getUserID());
            staffAccount.setShiftID(staffAccountRequest.getShiftID());
            staffAccount.setPhoneNumber(staffAccountRequest.getPhoneNumber());
            staffAccount.setStartDate(staffAccountRequest.getStartDate());
            staffAccount.setSalary(staffAccountRequest.getSalary());
            staffAccountRepository.save(staffAccount);
        }else{
            logger.warn("Staff account with ID {} not found.", staffAccountRequest.getStaffID());
        }
    }



    
}


