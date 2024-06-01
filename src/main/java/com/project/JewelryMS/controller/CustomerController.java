package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.CreateCustomerRequest;
import com.project.JewelryMS.model.CustomerRequest;
import com.project.JewelryMS.model.ViewCustomerPointRequest;
import com.project.JewelryMS.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/customer")
@SecurityRequirement(name = "api")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    //Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.ok(customer);
    }

    //Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Customer>>readAllCustomer(){
        List<Customer> customerList = customerService.readAllCustomer();
        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/list-by-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<Customer> readCustomerFromId(@RequestParam Long id){
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/list-by-number")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<Customer> readCustomerFromPhoneNumber(@RequestParam String id){
        Customer customer = customerService.getCustomerByPhoneNumber(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/list-active-customer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Customer>> readAllActiveCustomer() {
        List<Customer> customerList = customerService.readAllActiveCustomers(); // Filter active customers
        return ResponseEntity.ok(customerList);
    }


    //Delete section
    @PostMapping("/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<String> deleteCustomer(@RequestBody long id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer details marked as inactive successfully");
    }


    //Update section
    @PostMapping("/update-customer-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerRequest customerRequest){
        customerService.updateCustomerDetails(customerRequest);
        return ResponseEntity.ok("Customer Details updated successfully");
    }

    @PostMapping("/update-customer-points")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<String> updateCustomerPoints(@RequestBody ViewCustomerPointRequest viewPointsRequest){
        customerService.updateCustomerPoints(viewPointsRequest);
        return ResponseEntity.ok("Customer points updated successfully");
    }
}

