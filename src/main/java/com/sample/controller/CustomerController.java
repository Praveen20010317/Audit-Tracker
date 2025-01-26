package com.sample.controller;

import com.sample.model.Customer;
import com.sample.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestBody Customer customer) throws Exception {
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/checkCustomerId/{customerId}")
    public boolean existsByCustomerId(@PathVariable String customerId) {
        return customerService.existsByCustomerId(customerId);
    }

    @GetMapping("/checkMobileNo/{mobileNo}")
    public boolean existsByMobileNo(@PathVariable String mobileNo) {
        return customerService.existsByMobileNo(mobileNo);
    }

    @GetMapping("/checkCustomerEmailId/{emailId}")
    public boolean existsByEmailId(@PathVariable String emailId) {
        return customerService.existsByEmailId(emailId);
    }

    @GetMapping("/getAllCustomer")
    public List<String> getAllCustomer() {
        return customerService.getAllCustomer();
    }
}
