package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.CreateCustomerRequest;
import com.project.JewelryMS.model.CustomerRequest;
import com.project.JewelryMS.model.ViewCustomerPointRequest;
import com.project.JewelryMS.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();

        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setPointAmount(createCustomerRequest.getPointAmount());

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }
    public Customer getCustomerByPhoneNumber(String id) {
        return customerRepository.findByPhoneNumber(id).orElse(null);
    }


    public List<Customer> readAllCustomer() {
        return customerRepository.findAll();
    }

    public void updateCustomerDetails(CustomerRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getId());
        if(customerUpdate.isPresent()){

            Customer customer = customerUpdate.get();
            customer.setEmail(customerRequest.getEmail());
            customer.setPhoneNumber(customerRequest.getPhoneNumber());
            customer.setPointAmount(customerRequest.getPointAmount());
            customerRepository.save(customer);
        }
    }

    public void deleteCustomerById(long id) {
        Optional<Customer> customerUpdate = customerRepository.findById(id);
        customerUpdate.ifPresent(customer -> {
            customer.setStatus(false); // Set status to false
            customerRepository.save(customer);
        });
    }


    public void updateCustomerPoints(ViewCustomerPointRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getId());
        customerUpdate.ifPresent(customer -> {
            customer.setPointAmount(customerRequest.getPointAmount());
            customerRepository.save(customer);
        });
    }

    public List<Customer> readAllActiveCustomers() {
        // Retrieve only active customers (status = true)
        return customerRepository.findByStatus(true);
    }

}
