package com.sample.service;

import com.sample.model.Transaction;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface PdfReaderService {
    List<Transaction> readPdfFile(MultipartFile file) throws IOException;

    void exportStatement(HttpServletResponse response, List<Transaction> transactionList) throws IOException;
}
