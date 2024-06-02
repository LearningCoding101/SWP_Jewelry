package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ManagerAccountService {

    @Autowired
    ManagerAccountRepository managerAccountRepository;


    public List<ManagerAccountResponse> getAllManagerAccounts() {
        return managerAccountRepository.findAllManagerAccounts();
    }

    public ManagerAccountResponse getManagerAccountById(int id) {
        return managerAccountRepository.findManagerAccountById(id);
    }

}
