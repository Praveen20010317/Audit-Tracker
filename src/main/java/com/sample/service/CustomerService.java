package com.sample.service;

import com.sample.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerService {
    String saveCustomer(Customer customer) throws Exception;

    boolean existsByCustomerId(String customerId);

    boolean existsByMobileNo(String mobileNo);

    boolean existsByEmailId(String emailId);

    List<String> getAllCustomer();
}
