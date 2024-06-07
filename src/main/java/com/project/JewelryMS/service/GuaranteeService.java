package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.model.Performance.CreatePerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceResponse;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuaranteeService {
    @Autowired
    GuaranteeRepository guaranteeRepository;
    @Autowired
    ProductSellRepository productSellRepository;



    public GuaranteeResponse createGuarantee(CreateGuaranteeRequest createGuaranteeRequest) {
        //the staffAccountRepository could have a JpaRepository of <Integer>
        Optional<ProductSell> productSellOptional = productSellRepository.findById((long)createPerformanceRequest.getStaffID());
        if(staffOptional.isPresent()) {
            StaffAccount account = staffOptional.get();
            if (account != null) {//This if condition needs revision, should use to connect with Shift to accurately create a performance report
                Performance performance = new Performance();

                performance.setDate(createPerformanceRequest.getDate());
                performance.setSalesMade(createPerformanceRequest.getSalesMade());
                performance.setRevenueGenerated(createPerformanceRequest.getRevenueGenerated());
                performance.setCustomerSignUp(createPerformanceRequest.getCustomerSignUp());

                performance.setStaffAccount(account);

                Performance newPerformance = performanceRepository.save(performance);
                return getPerformanceByStaffID(newPerformance.getStaffAccount().getStaffID());
            }else{
                throw new RuntimeException("...");
            }
        }else{
            throw new RuntimeException("Staff account ID not exist");
        }
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
