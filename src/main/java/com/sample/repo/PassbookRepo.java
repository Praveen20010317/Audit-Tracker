package com.sample.repo;

import com.sample.model.Passbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassbookRepo extends JpaRepository<Passbook, String> {

    Passbook findAllByTransactionId(String customerId);
}
