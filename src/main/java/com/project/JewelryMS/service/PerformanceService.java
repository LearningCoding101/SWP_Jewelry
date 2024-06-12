package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Performance;
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
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    // Helper method to convert a Performance entity to a PerformanceResponse DTO
    private PerformanceResponse toPerformanceResponse(Performance performance) {
        return new PerformanceResponse(
                performance.getPK_performanceID(),
                performance.getStaffAccount().getStaffID(),
                performance.getDate(),
                performance.getSalesMade(),
                performance.getRevenueGenerated(),
                performance.getCustomerSignUp(),
                performance.isStatus()
        );
    }

    public PerformanceResponse createPerformanceReport(CreatePerformanceRequest createPerformanceRequest) {
        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(Math.toIntExact((long) createPerformanceRequest.getStaffID()));
        if(staffOptional.isPresent()) {
            StaffAccount account = staffOptional.get();
            if (account != null) {
                Performance performance = new Performance();

                performance.setDate(createPerformanceRequest.getDate());
                performance.setSalesMade(createPerformanceRequest.getSalesMade());
                performance.setRevenueGenerated(createPerformanceRequest.getRevenueGenerated());
                performance.setCustomerSignUp(createPerformanceRequest.getCustomerSignUp());

                performance.setStaffAccount(account);

                Performance newPerformance = performanceRepository.save(performance);
                return toPerformanceResponse(newPerformance);
            }else{
                throw new RuntimeException("...");
            }
        }else{
            throw new RuntimeException("Staff account ID not exist");
        }
    }

    public PerformanceResponse getPerformanceById(long id) {
        Performance performance = performanceRepository.findByPerformanceId(id);
        return performance != null ? toPerformanceResponse(performance) : null;
    }

    public PerformanceResponse getPerformanceByStaffID(long id) {
        Performance performance = performanceRepository.findByStaffId(id);
        return performance != null ? toPerformanceResponse(performance) : null;
    }

    public List<PerformanceResponse> getPerformanceByDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        List<Performance> performances = performanceRepository.findByDate(formattedDate);

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }

    public List<PerformanceResponse> readAllPerformanceReport() {
        List<Performance> performances = performanceRepository.listAll();

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }

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

    public List<PerformanceResponse> readAllConfirmedPerformanceReport() {
        List<Performance> performances = performanceRepository.findByStatus(true);

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }
}

