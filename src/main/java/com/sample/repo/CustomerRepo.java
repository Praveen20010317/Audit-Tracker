package com.sample.repo;

import com.sample.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {
    boolean existsByCustomerId(String customerId);

    boolean existsByEmailId(String emailId);

    boolean existsByMobileNo(String mobileNo);

    Customer findByCustomerId(String customerId);

    Customer findByCustomerName(String customerName);
}
