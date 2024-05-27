package com.shop.JewleryMS.Controller;


import com.shop.JewleryMS.Entity.Customer;
import com.shop.JewleryMS.Model.CreateCustomerRequest;
import com.shop.JewleryMS.Model.CustomerRequest;
import com.shop.JewleryMS.Model.ViewCustomerPointRequest;
import com.shop.JewleryMS.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/Customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/CreateCustomer")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.ok(customer);
    }
    @GetMapping("/ReadAllCustomer")
    public ResponseEntity<List<Customer>>readAllCustomer(){
        List<Customer> customerList = customerService.readAllCustomer();
        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/ReadCustomerId")
    public ResponseEntity<Customer> readCustomerFromId(@RequestBody long id){
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/Delete")
    public ResponseEntity<String> deleteCustomer(@RequestBody long id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer details deleted successfully");
    }

    @PostMapping("/UpdateCustomerDetails")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerRequest customerRequest){
        customerService.updateCustomerDetails(customerRequest);
        return ResponseEntity.ok("Customer Details updated successfully");
    }

    @PostMapping("/UpdateCustomerPoints")
    public ResponseEntity<String> updateCustomerPoints(@RequestBody ViewCustomerPointRequest viewPointsRequest){
        customerService.updateCustomerPoints(viewPointsRequest);
        return ResponseEntity.ok("Customer points updated successfully");
    }

}

