package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyalty;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUp;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.service.DashboardService;
import com.project.JewelryMS.service.TransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/Dashboard")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private TransitionService transitionService;

    @GetMapping("/purchase-order-history")
    public ResponseEntity<List<TransitionResponse>> getPurchaseOrderHistory() {
        List<TransitionResponse> purchaseOrderHistory = transitionService.getPurchaseOrderHistory();
        return ResponseEntity.ok(purchaseOrderHistory);
    }

    @GetMapping("revenue-category")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(revenueDateRequest));
    }

    @GetMapping("revenue-category-all")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory() {
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory();
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products-all")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts());
    }

    @GetMapping("/loyalty-customers")
    public ResponseEntity<List<CustomerLoyalty>> getCustomerLoyaltyStatistics(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerLoyaltyStatistics(revenueDateRequest));
    }

    @GetMapping("demographic-customers")
    public ResponseEntity<List<CustomerDemographics>> getCustomerDemoGraphic(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerDemoGraphicResponse(revenueDateRequest));
    }

    @GetMapping("/customer-signups")
    public ResponseEntity<List<CustomerSignUp>> getCustomerSignUpsByStaff(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerSignUpsByStaff(revenueDateRequest));
    }

    @GetMapping("compare-day")
    public ComparisonResponse compareDay(@RequestParam("date1") String date1, @RequestParam("date2") String date2) {
        DayComparisonRequest request = new DayComparisonRequest(LocalDate.parse(date1), LocalDate.parse(date2));
        return dashboardService.compareDay(request);
    }

    @GetMapping("compare-month")
    public ComparisonResponse compareMonth(@RequestParam("month1") String month1, @RequestParam("month2") String month2) {
        MonthComparisonRequest request = new MonthComparisonRequest(month1, month2);
        return dashboardService.compareMonth(request);
    }

    @GetMapping("compare-year")
    public ComparisonResponse compareYear(@RequestParam("year1") String year1, @RequestParam("year2") String year2) {
        YearComparisonRequest request = new YearComparisonRequest(year1, year2);
        return dashboardService.compareYear(request);
    }

    @GetMapping("/revenue-by-staff")
    public ResponseEntity<List<StaffRevenueResponse>> getRevenueByStaff(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<StaffRevenueResponse> revenueByStaff = dashboardService.getRevenueGeneratedByStaff(localDate);
        return ResponseEntity.ok(revenueByStaff);
    }


}
