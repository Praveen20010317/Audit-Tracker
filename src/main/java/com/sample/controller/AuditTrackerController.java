package com.sample.controller;

import com.sample.model.Transaction;
import com.sample.service.PdfReaderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/audit-tracker")
public class AuditTrackerController {

    @Autowired
    PdfReaderService pdfReaderService;

    @PostMapping("/file_upload")
    public void uploadPdf(HttpServletResponse response, @RequestParam("file")MultipartFile file) throws Exception {
        if (file.isEmpty()) {
             throw new Exception("File not Uploaded !");
        } else {
            List<Transaction> transactionList = pdfReaderService.readPdfFile(file);
            System.out.println(transactionList.get(0).getDate());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=statement.xlsx");
            pdfReaderService.exportStatement(response, transactionList);
        }
    }
}
