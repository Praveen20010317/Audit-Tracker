package com.sample.service;

import com.sample.dto.PassbookDto;
import com.sample.request.PassbookRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PassbookService {
    String saveTransaction(PassbookRequest passbook) throws Exception;

    String updateTransaction(PassbookRequest passbook) throws Exception;

    List<PassbookDto> getAllTransaction();

    String deleteTransaction(String transactionId);
}
