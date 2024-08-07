package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyalty;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerPurchaseHistoryResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUp;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.service.DashboardService;
import com.project.JewelryMS.service.TransitionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Dashboard")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private TransitionService transitionService;

    @GetMapping("/purchase-order-history")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<TransitionResponse>> getPurchaseOrderHistory() {
        List<TransitionResponse> purchaseOrderHistory = transitionService.getPurchaseOrderHistory();
        return ResponseEntity.ok(purchaseOrderHistory);
    }

    @GetMapping("revenue-category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(revenueDateRequest));
    }

    @GetMapping("revenue-category-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory() {
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory();
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts());
    }

    @GetMapping("/loyalty-customers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerLoyalty>> getCustomerLoyaltyStatistics(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerLoyaltyStatistics(revenueDateRequest));
    }

    @GetMapping("demographic-customers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerDemographics>> getCustomerDemoGraphic(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerDemoGraphicResponse(revenueDateRequest));
    }

    @GetMapping("/customer-signups")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerSignUp>> getCustomerSignUpsByStaff(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerSignUpsByStaff(revenueDateRequest));
    }


    @GetMapping("compare-sale-year")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public YearComparisonResponse comparationYear(@RequestParam("startYear") String year1, @RequestParam("endYear") String year2) {
        YearComparisonRequest request = new YearComparisonRequest(year1, year2);
        return dashboardService.compareYear(request);
    }
    @GetMapping("/revenueByStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<StaffRevenueResponse>> getRevenueGeneratedByStaff(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<StaffRevenueResponse> response = dashboardService.getRevenueGeneratedByStaff(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }
    @GetMapping("discount-code-effectiveness")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<DiscountEffectivenessResponse> getDiscountCodeEffectiveness() {
        return dashboardService.getDiscountCodeEffectiveness();
    }


    @GetMapping("/salesByStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<StaffSalesResponse>> getSalesMadeByStaff(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<StaffSalesResponse> response = dashboardService.getSalesMadeByStaff(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("/customer-products-trend")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<ProductSellTrendResponse>> getTopProductsForAllCustomers() {
        List<ProductSellTrendResponse> responses = dashboardService.getTopProductsForAllCustomers();
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/monthlyAverageRevenue")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Map<String, Float>> getMonthlyAverageRevenue(
            @RequestParam String startMonthYear, @RequestParam String endMonthYear) {
        Map<String, Float> dailyAverageRevenue = dashboardService.getDailyAverageRevenuePerMonth(startMonthYear, endMonthYear);
        if (dailyAverageRevenue.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(dailyAverageRevenue);
        }
    }

    @GetMapping("/daily-revenue")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Map<String, Double>> getDailyRevenue(
            @RequestParam String startDate, @RequestParam String endDate) {
        try {
            Map<String, Double> dailyRevenue = dashboardService.getDailyRevenue(startDate, endDate);
            return ResponseEntity.ok(dailyRevenue);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/customer-purchase-history")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerPurchaseHistoryResponse>> getAllCustomerPurchaseHistories() {
        List<CustomerPurchaseHistoryResponse> response = dashboardService.getAllCustomerPurchaseHistories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff-statistics")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<StaffStatisticsResponse> getStaffStats(@RequestParam("staffId") long staffId) {
        StaffStatisticsResponse response = dashboardService.getStaffStats(staffId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff-statistics-range")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<StaffStatisticsResponse> getStaffStatsInRange(
            @RequestParam("staffEmail") String staffEmail,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        StaffStatisticsResponse response = dashboardService.getStaffStatsInRange(staffEmail, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workArea-statistics-year")
    public ResponseEntity<List<WorkAreaRevenueResponse>> getWorkAreaStatisticsYear(){
        List<WorkAreaRevenueResponse> workAreaRevenueResponse = dashboardService.getWorkAreaRevenueResponsesByYear();
        return ResponseEntity.ok(workAreaRevenueResponse);
    }

    @GetMapping("/workArea-statistics-month")
    public ResponseEntity<List<WorkAreaRevenueResponse>> getWorkAreaStatisticsMonth(){
        List<WorkAreaRevenueResponse> workAreaRevenueResponse = dashboardService.getWorkAreaRevenueResponsesByMonth();
        return ResponseEntity.ok(workAreaRevenueResponse);
    }

    @GetMapping("/workArea-statistics-day")
    public ResponseEntity<List<WorkAreaRevenueResponse>> getWorkAreaStatisticsDay(){
        List<WorkAreaRevenueResponse> workAreaRevenueResponse = dashboardService.getWorkAreaRevenueResponsesByDay();
        return ResponseEntity.ok(workAreaRevenueResponse);
    }

}
