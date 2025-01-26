package com.sample.controller;

import com.sample.dto.PassbookDto;
import com.sample.model.Customer;
import com.sample.model.Passbook;
import com.sample.model.Transaction;
import com.sample.request.PassbookRequest;
import com.sample.service.PassbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passbook")
public class PassbookController {

    @Autowired
    private final PassbookService passbookService;

    public PassbookController(PassbookService passbookService) {
        this.passbookService = passbookService;
    }

    @PostMapping("/saveTransaction")
    public String saveTransaction(@RequestBody PassbookRequest passbook) throws Exception {
        return passbookService.saveTransaction(passbook);
    }

    @PutMapping("/updateTransaction")
    public String updateTransaction(@RequestBody PassbookRequest passbook ) throws Exception {
        return passbookService.updateTransaction(passbook) ;
    }
    @GetMapping("/viewTransaction")
    public List<PassbookDto> getAllTransaction() {
        return passbookService.getAllTransaction();
    }

    @GetMapping("/filterByCustomer")
    public List<PassbookDto> filterByCustomer() {
        return passbookService.getAllTransaction();
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public String deleteTransaction(@PathVariable String transactionId) {
        return passbookService.deleteTransaction(transactionId);
    }
}
