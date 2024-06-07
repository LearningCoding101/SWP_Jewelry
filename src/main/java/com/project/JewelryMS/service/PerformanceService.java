package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Performance.CreatePerformanceRequest;
import com.project.JewelryMS.model.Performance.DeletePerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceResponse;

import com.project.JewelryMS.repository.PerformanceRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {
    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    public PerformanceResponse createPerformanceReport(CreatePerformanceRequest createPerformanceRequest) {
//        Performance performance = new Performance();
//
//        performance.setDate(createPerformanceRequest.getDate());
//        performance.setSalesMade(createPerformanceRequest.getSalesMade());
//        performance.setRevenueGenerated(createPerformanceRequest.getRevenueGenerated());
//        performance.setCustomerSignUp(createPerformanceRequest.getCustomerSignUp());
//
//        //Use the Staff Accounts
//        StaffAccount staffAccount = createPerformanceRequest.getStaffAccount();
//        if (staffAccount != null) {
//            performance.setStaffAccount(staffAccount);
//        } else {
//            throw new RuntimeException("Staff account information is missing in the request");
//        }
//        return performanceRepository.save(performance);

        //the staffAccountRepository could have a JpaRepository of <Integer>
        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(Long.valueOf(createPerformanceRequest.getStaffID()));
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

    public PerformanceResponse getPerformanceById(long id) {
        Optional<PerformanceResponse> staffAccountOptional = performanceRepository.findByPerformanceId(id);
        return staffAccountOptional.orElse(null);
    }

    public PerformanceResponse getPerformanceByStaffID(long id) {
        Optional<PerformanceResponse> staffAccountOptional = performanceRepository.findByStaffId(id);
        return staffAccountOptional.orElse(null);
    }

    public List<PerformanceResponse> getPerformanceByDate(Date date) {
        // Convert the Date to a formatted String (yyyy-MM-dd)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        // Call the repository method with the formatted date
        List<PerformanceResponse> performance = performanceRepository.findByDate(formattedDate);

        return performance;
    }



    public List<PerformanceResponse> readAllPerformanceReport() {
        return performanceRepository.listAll();
    }

    //Note: Updates should be automatically during the current shift/daytime
    //This is just a basic iteration on how Performance will be created, and should be updated once the shift is ready
    public Performance updatePerformanceReportDetails(PerformanceRequest performanceRequest) {
        Optional<Performance> performanceUpdate = performanceRepository.findById(performanceRequest.getPK_performanceID());


        if(performanceUpdate.isPresent()){

            Performance performance = performanceUpdate.get();

            performance.setDate(performanceRequest.getDate());
            performance.setSalesMade(performanceRequest.getSalesMade());
            performance.setRevenueGenerated(performanceRequest.getRevenueGenerated());
            performance.setCustomerSignUp(performanceRequest.getCustomerSignUp());

            return performanceRepository.save(performance);
        }else {
            throw new RuntimeException("performance report with ID " + performanceRequest.getPK_performanceID() + " not found");
        }
    }

    /*
    Note:
    By default the status is true, if the manager decide it is not right
    (Reasons can be wrong station, misconduct)
    Then, the manager can decide if the Performance report is invalid
    */
//Unused version
//    public void deletePerformanceById(long id) {
//        Optional<Performance> performanceUpdate = performanceRepository.findById(id);
//        performanceUpdate.ifPresent(report -> {
//            report.setStatus(false); // Set status to false
//            performanceRepository.save(report);
//        });
//    }
    public void deletePerformanceById(DeletePerformanceRequest deletePerformanceReportRequest) {
        Optional<Performance> performanceOptional = performanceRepository.findById(deletePerformanceReportRequest.getPK_performanceID());
        if (performanceOptional.isPresent()) {
            Performance performance = performanceOptional.get();
            if (performance != null) {
                boolean status=false;
                performance.setStatus(status);
                performanceRepository.save(performance);
            } else {
                throw new RuntimeException("Performance Report with ID:  " + deletePerformanceReportRequest.getPK_performanceID() + " not found");
            }
        } else {
            throw new RuntimeException("Performance ID" + deletePerformanceReportRequest.getPK_performanceID()+ " not found");
        }
    }

//    public void deletePerformanceByStaffID(long id) {
//        Optional<PerformanceResponse> performanceUpdate = performanceRepository.findByStaffID(id);
//        performanceUpdate.ifPresent(report -> {
//            report.setStatus(false); // Set status to false
//            performanceRepository.save(report);
//        });
//    }

    public List<PerformanceResponse> readAllConfirmedPerformanceReport() {
        // Retrieve only reports considered to be valid(status = true)
        return performanceRepository.findByStatus(true);
    }

}
