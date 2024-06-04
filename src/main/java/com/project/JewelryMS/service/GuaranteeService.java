package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.repository.CustomerRepository;
import com.project.JewelryMS.repository.GuaranteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuaranteeService {
    @Autowired
    GuaranteeRepository guaranteeRepository;

    public Guarantee createGuarantee(CreateGuaranteeRequest createGuaranteeRequest) {
        Guarantee guarantee = new Guarantee();

        guarantee.setFK_productID(createGuaranteeRequest.getFK_productID());
        guarantee.setCoverage(createGuaranteeRequest.getCoverage());
        guarantee.setPolicyType(createGuaranteeRequest.getPolicyType());

        return guaranteeRepository.save(guarantee);
    }

    public Guarantee getGuaranteeById(long id) {
        return guaranteeRepository.findById(id).orElse(null);
    }

    public List<Guarantee> readAllGuarantee() {
        return guaranteeRepository.findAll();
    }

    public void updateGuaranteeDetails(GuaranteeRequest guaranteeRequest) {
        Optional<Guarantee> guaranteeUpdate = guaranteeRepository.findById(guaranteeRequest.getPK_guaranteeID());
        if(guaranteeUpdate.isPresent()){
            Guarantee guarantee = guaranteeUpdate.get();

            guarantee.setFK_productID(guaranteeRequest.getFK_productID());
            guarantee.setCoverage(guaranteeRequest.getCoverage());
            guarantee.setPolicyType(guaranteeRequest.getPolicyType());
            guaranteeRepository.save(guarantee);
        }
    }

    public void deleteGuaranteePolicyById(long id) {
        Optional<Guarantee> customerUpdate = guaranteeRepository.findById(id);
        customerUpdate.ifPresent(customer -> {
            customer.setStatus(false); // Set status to false
            guaranteeRepository.save(customer);
        });
    }

    public List<Guarantee> readAllActiveGuaranteePolicy() {
        // Retrieve only active customers (status = true)
        return guaranteeRepository.findByStatus(true);
    }

}
