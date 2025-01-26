package com.sample.serviceImpl;

import com.sample.dto.PassbookDto;
import com.sample.model.Customer;
import com.sample.model.Passbook;
import com.sample.repo.CustomerRepo;
import com.sample.repo.PassbookRepo;
import com.sample.request.PassbookRequest;
import com.sample.service.PassbookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassbookServiceImpl implements PassbookService {

    private final PassbookRepo passbookRepo;
    private final CustomerRepo customerRepo;

    public PassbookServiceImpl(PassbookRepo passbookRepo, CustomerRepo customerRepo) {
        this.passbookRepo = passbookRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public String saveTransaction(PassbookRequest passbook) throws Exception {
        Passbook passbook1 = new Passbook();
        try {
            Customer customer = customerRepo.findByCustomerName(passbook.getCustomerName());
            if (passbook.getTransactionType().equals("credit")) {
                passbook1.setOpeningBalance(customer.getAvailableBalance());
                passbook1.setClosingBalance(customer.getAvailableBalance() + passbook.getTransactionAmount());
                customer.setAvailableBalance(customer.getAvailableBalance() + passbook.getTransactionAmount());
            } else {
                passbook1.setOpeningBalance(customer.getAvailableBalance());
                passbook1.setClosingBalance(customer.getAvailableBalance() - passbook.getTransactionAmount());
                customer.setAvailableBalance(customer.getAvailableBalance() - passbook.getTransactionAmount());
            }
            passbook1.setTransactionType(passbook.getTransactionType());
            passbook1.setTransactionAmount(passbook.getTransactionAmount());
            passbook1.setModeOfTransaction(passbook.getModeOfTransaction());
            passbook1.setTransactionDate(passbook.getTransactionDate());
            passbook1.setRemarks(passbook.getRemarks());
            passbook1.setCustomer(customer);
            Passbook passbook2 = passbookRepo.save(passbook1);
            String formattedId = String.format("TXN%06d", passbook2.getId());
            passbook2.setTransactionId(formattedId);
            passbookRepo.save(passbook2);
            return "New transaction added Successfully !";
        } catch (Exception e) {
            throw new Exception("Unable to add transaction !");
        }
    }

    @Override
    public String updateTransaction(PassbookRequest passbook) throws Exception {
        Customer customer = customerRepo.findByCustomerName(passbook.getCustomerName());
        if (passbook.getTransactionType().equals("Credit")) {
            customer.setAvailableBalance(customer.getAvailableBalance() + passbook.getTransactionAmount());
        } else {
            customer.setAvailableBalance(customer.getAvailableBalance() - passbook.getTransactionAmount());
        }
        Passbook passbook1 = passbookRepo.findAllByTransactionId(passbook.getCustomerName());
        passbook1.setTransactionType(passbook.getTransactionType());
        passbook1.setTransactionAmount(passbook.getTransactionAmount());
        passbook1.setModeOfTransaction(passbook.getModeOfTransaction());
        passbook1.setTransactionDate(passbook.getTransactionDate());
        passbook1.setRemarks(passbook.getRemarks());
        passbook1.setCustomer(customer);
        Passbook passbook2 = passbookRepo.save(passbook1);
        if (passbook1.equals(passbook2)) {
            return "New transaction added Successfully !";
        } else {
            throw new Exception("Unable to update transaction !");
        }
    }

    @Override
    public List<PassbookDto> getAllTransaction() {
        List<PassbookDto> dtoList = new ArrayList<>();
        List<Passbook> passbookList = passbookRepo.findAll();
        for (Passbook passbook : passbookList) {
            PassbookDto dto = new PassbookDto();
            dto.setTransactionId(passbook.getTransactionId());
            dto.setTransactionType(passbook.getTransactionType());
            dto.setTransactionDate(passbook.getTransactionDate());
            dto.setModeOfTransaction(passbook.getModeOfTransaction());
            dto.setTransactionAmount(passbook.getTransactionAmount());
            dto.setRemarks(passbook.getRemarks());
            dto.setCustomerId(passbook.getCustomer().getCustomerId());
            dto.setCustomerName(passbook.getCustomer().getCustomerName());
            dto.setOpeningBalance(passbook.getOpeningBalance());
            dto.setClosingBalance(passbook.getClosingBalance());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public String deleteTransaction(String transactionId) {
        Passbook passbook = passbookRepo.findAllByTransactionId(transactionId);
        passbookRepo.delete(passbook);
        return "Transaction removed successfully !";
    }
}
