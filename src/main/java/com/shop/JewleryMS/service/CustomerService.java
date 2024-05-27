package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.Customer;
import com.shop.JewleryMS.Model.CreateCustomerRequest;
import com.shop.JewleryMS.Model.CustomerRequest;
import com.shop.JewleryMS.Model.ViewCustomerPointRequest;
import com.shop.JewleryMS.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();

        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setPointsAmount(createCustomerRequest.getPointsAmount());

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
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
            customer.setPointsAmount(customerRequest.getPointsAmount());
            customerRepository.save(customer);
        }
    }

    public void deleteCustomerById(long id) {
        customerRepository.deleteById(id);
    }

    public void updateCustomerPoints(ViewCustomerPointRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getId());
        customerUpdate.ifPresent(customer -> {
            customer.setPointsAmount(customerRequest.getPointsAmount());
            customerRepository.save(customer);
        });
    }
}
