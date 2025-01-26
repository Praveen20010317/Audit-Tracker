package com.sample.serviceImpl;

import com.sample.model.Customer;
import com.sample.repo.CustomerRepo;
import com.sample.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public String saveCustomer(Customer customer) throws Exception {
        Customer customer1 = customerRepo.save(customer);
        if (customer.equals(customer1)) {
            return "New Customer added Successfully !" ;
        } else {
            throw new Exception("Error occured while adding Customer !");
        }
    }

    @Override
    public boolean existsByCustomerId(String customerId) {
        return customerRepo.existsByCustomerId(customerId);
    }

    @Override
    public boolean existsByMobileNo(String mobileNo) {
        return customerRepo.existsByMobileNo(mobileNo);
    }

    @Override
    public boolean existsByEmailId(String emailId) {
        return customerRepo.existsByEmailId(emailId);
    }

    @Override
    public List<String> getAllCustomer() {
        List<String> customerName = new ArrayList<>();
        List<Customer> customerList = customerRepo.findAll();
        for (Customer customer : customerList) {
            customerName.add(customer.getCustomerName());
        }
        return customerName;
    }
}
