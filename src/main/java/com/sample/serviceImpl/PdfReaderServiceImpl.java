package com.sample.serviceImpl;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.sample.model.Transaction;
import com.sample.service.PdfReaderService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfReaderServiceImpl implements PdfReaderService {

    @Override
    public List<Transaction> readPdfFile(MultipartFile file) throws IOException {
        try {
            // Load the PDF document
//            File file = new File("C:/Users/prave/Downloads/abc feb.pdf");
            InputStream inputStream = file.getInputStream();
            PDDocument document = PDDocument.load(inputStream);
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            String outputText = processText(text);
            List<Transaction> transactionList = new ArrayList<>();
//            document.close();
            // Get the total number of pages in the PDF document
            int totalPages = document.getNumberOfPages();

            // Iterate over each page and process it
            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                // Set the range for the current page
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(pageNum + 1);  // Page numbers are 1-based in PDFTextStripper
                stripper.setEndPage(pageNum + 1);

                // Extract text from the current page
                String pageText = stripper.getText(document);

                // Process the page line by line
                List<Transaction> pageTransactions = splitTransaction(processText(pageText));
                transactionList.addAll(pageTransactions);
//                System.out.println(processText(pageText));
            }

            document.close();

            return transactionList;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void exportStatement(HttpServletResponse response, List<Transaction> transactionList) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=statement.xlsx");

        SXSSFWorkbook workbook = new SXSSFWorkbook(100); // Keep 100 rows in memory at a time
        Sheet sheet = workbook.createSheet("Data");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"DATE", "PARTICULARS", "NAME", "CHQ.NO.", "WITHDRAWALS", "DEPOSITS", "BALANCE", "TRANSACTION TYPE", "MODE OF TRANSACTION"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Populate the rows with data from the transaction list
        int rowNum = 1;
        for (Transaction item : transactionList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getDate());
            row.createCell(1).setCellValue(item.getDescription());
            row.createCell(2).setCellValue(item.getName());
            row.createCell(3).setCellValue(item.getChqNo());
            row.createCell(4).setCellValue(item.getWithdrawals());
            row.createCell(5).setCellValue(item.getDeposits());
            row.createCell(6).setCellValue(item.getBalance());
            row.createCell(7).setCellValue(item.getTransactionType());
            row.createCell(8).setCellValue(item.getModeOfTransaction());
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        }
        workbook.close();
    }

    private static String processText(String inputText) {
        String[] lines = inputText.split("\n");
        StringBuilder result = new StringBuilder();
        String datePattern = "\\d{2}-[A-Z]{3}-\\d{2}";
        String datePattern2 = "\\d{2}-\\d{2}-\\d{4}";
        Pattern pattern = Pattern.compile(datePattern);
        Pattern pattern2 = Pattern.compile(datePattern2);
        StringBuilder currentLine = new StringBuilder();
        for (String line : lines) {
            line = line.trim();
            if (line.contains(",")) {
                continue;
            }
            if (line.contains("Branch Address :")) {
                continue;
            }
            if (line.contains("DATE PARTICULARS CHQ.NO. WITHDRAWALS DEPOSITS BALANCE")) {
                continue;
            }
            line = line.replaceAll("(?<=Cr)(?=\\S)", " ");
            // Match if the line starts with a date pattern
            Matcher matcher = pattern.matcher(line);
            Matcher matcher2 = pattern2.matcher(line);
            if (matcher2.find()) {
                if (!currentLine.isEmpty()) {
                    result.append(currentLine.toString()).append("\n");
                }
                // Start a new transaction line with the current date
                currentLine.setLength(0); // Reset the currentLine
                currentLine.append(line);
            }
            // If the line contains a date
            else if (matcher.find()) {
                // If there's already a line accumulated, add it to the result
                if (!currentLine.isEmpty()) {
                    result.append(currentLine.toString()).append("\n");
                }
                // Start a new transaction line with the current date
                currentLine.setLength(0); // Reset the currentLine
                currentLine.append(line);
            } else {
                // If there's no date, append the current line to the transaction
                if (!currentLine.isEmpty()) {
                    currentLine.append(" ").append(line);
//                    currentLine.append(line);
                }
            }
        }
        // Append the last accumulated line
        if (!currentLine.isEmpty()) {
            result.append(currentLine.toString());
        }
        return result.toString().trim();  // Trim to remove any trailing newlines
    }

    public static List<Transaction> splitTransaction(String extractedText) {
        String regexPattern = "(\\d{2}-\\w{3}-\\d{2})(\\s+([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))?\\s+([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr)\\s+(.+)";
//        String regexPattern = "(\\d{2}-\\w{3}-\\d{2})\\s*(?:([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))?\\s+([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr)\\s+(.+?)\\s*(\\d{5,6})?$";

        String firstLineP1 = "(\\d{2}-\\d{2}-\\d{4})\\s+(([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))\\s+(Brought\\s+Forward)";
        String firstLineP2 = "(\\d{2}-\\w{3}-\\d{2})\\s+(([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))\\s+(Brought\\s+Forward)";
        String lastLineP1 = "(\\d{2}-\\d{2}-\\d{4})\\s+(([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))\\s+(Carried\\s+Forward)";
        String lastLineP2 = "(\\d{2}-\\w{3}-\\d{2})\\s+(([-+]?\\d+\\.\\d{2})\\s+(Cr|Dr))\\s+(Carried\\s+Forward)";
        Pattern firstLinePtn1 = Pattern.compile(firstLineP1);
        Pattern firstLinePtn2 = Pattern.compile(firstLineP2);
        Pattern lastLinePtn1 = Pattern.compile(lastLineP1);
        Pattern lastLinePtn2 = Pattern.compile(lastLineP2);
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(extractedText);
        List<Transaction> transactionList = new ArrayList<>();

//        while (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                System.out.println("Group " + i + ": " + matcher.group(i));
//            }
//        }
        if (extractedText.contains("Brought Forward")) {
            Matcher m1 = firstLinePtn1.matcher(extractedText);
            Matcher m2 = firstLinePtn2.matcher(extractedText);
            if (m1.find()) {
                String date = m1.group(1);
                String description = m1.group(5);
                String balance = m1.group(3);
                transactionList.add(new Transaction(date, description, "", "", "", balance, "", "", ""));
            }
            if (m2.find()) {
                String date = m2.group(1);
                String description = m2.group(5);
                String balance = m2.group(3);
                transactionList.add(new Transaction(date, description, "", "", "", balance, "", "", ""));
            }
        }
        while (matcher.find()) {
            String date = matcher.group(1);
            String[] parts = matcher.group(7).trim().split(" ");
            String chequeNo = parts[parts.length - 1].matches("\\d{5,6}") ? parts[parts.length - 1] : "";
            String description = chequeNo.equals("") ? matcher.group(7) : matcher.group(7).substring(0, matcher.group(7).lastIndexOf(" "));
            String amount = (matcher.group(3) == null || matcher.group(3).isEmpty()) ? "" : matcher.group(3);
            String type = matcher.group(4);
            String balance = matcher.group(5);
            String modeOfTransaction = getModeOfTransaction(chequeNo, description);
            String name = getNameFromTransaction(chequeNo, description);
            if (!description.contains("Brought Forward") && !description.contains("Carried Forward")) {
                if ("Dr".equals(type)) {
                    transactionList.add(new Transaction(date.replace("\n", ""), description.replace("\n", ""), chequeNo.replace("\n", ""), amount.replace("\n", ""), "", balance.replace("\n", ""), "Debit", modeOfTransaction, name));
                } else {
                    transactionList.add(new Transaction(date.replace("\n", ""), description.replace("\n", ""), chequeNo.replace("\n", ""), "", amount.replace("\n", ""), balance.replace("\n", ""), "Credit", modeOfTransaction, name));
                }
            }
        }
        if (extractedText.contains("Carried Forward")) {
            Matcher m3 = lastLinePtn1.matcher(extractedText);
            Matcher m4 = lastLinePtn2.matcher(extractedText);
            if (m3.find()) {
                String date = m3.group(1);
                String description = m3.group(5);
                String balance = m3.group(3);
                transactionList.add(new Transaction(date, description, "", "", "", balance, "", "", ""));
            }
            if (m4.find()) {
                String date = m4.group(1);
                String description = m4.group(5);
                String balance = m4.group(3);
                transactionList.add(new Transaction(date, description, "", "", "", balance, "", "", ""));
            }
        }

        return transactionList;
    }

    private static String getNameFromTransaction(String chequeNo, String description) {
        description = description.trim();
        if (description.startsWith("UPI")) {
            return description.substring(17);
        }
        if (description.startsWith("NSB")) {
            return description.substring(28);
        }
        if (description.startsWith("RSB")) {
            return description.substring(28);
        }
        if (description.startsWith("NN")) {
            return description.substring(28);
        }
        if (description.startsWith("RBK")) {
            return description.substring(28);
        }
        if (description.startsWith("TRANSFER FROM ")) {
            return description.substring(14);
        }
        if (description.startsWith("IMPS")) {
            return description.substring(23);
        }
        if (description.startsWith("TRF FRM ")) {
            return description.substring(8);
        }
        if (description.startsWith("TRANSFER TO ")) {
            return description.substring(12);
        } else if (description.startsWith("TRF TO ")) {
            return description.substring(7);
        } else if (!chequeNo.isEmpty()) {
            return description;
        }
        if (description.length() > 28) {
            return description.substring(28);
        }
        if (!description.toLowerCase().contains("cheque")) {
            return description;
        }
        return "";
    }

    private static String getModeOfTransaction(String chequeNo, String description) {
        String modeOfTransaction = "";
        if (!chequeNo.isEmpty() || description.startsWith("CHEQUE")) {
            modeOfTransaction = "Cheque";
        } else if (description.startsWith("IMPS")) {
            modeOfTransaction = "IMPS";
        } else if (description.startsWith("UPI")) {
            modeOfTransaction = "UPI";
        } else {
            modeOfTransaction = "Fund Transfer";
        }
        return modeOfTransaction;
    }
}
