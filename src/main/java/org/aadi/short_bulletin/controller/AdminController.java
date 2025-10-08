package org.aadi.short_bulletin.controller;

import org.aadi.short_bulletin.entity.Bulletin;
import org.aadi.short_bulletin.service.PdfProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;  // Changed from @RestController to @Controller
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;  // Already imported
import org.springframework.web.bind.annotation.ResponseBody;  // Add this import

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Controller  // Changed
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final PdfProcessingService pdfProcessingService;

    public AdminController(PdfProcessingService pdfProcessingService) {
        this.pdfProcessingService = pdfProcessingService;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "admin-upload";  // Returns Thymeleaf view
    }

    @PostMapping("/process")
    @ResponseBody  // Added to make this method return text/JSON, not view
    public ResponseEntity<String> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            if (file.isEmpty()) {
                logger.error("No file uploaded");
                return ResponseEntity.badRequest().body("No file uploaded");
            }
            logger.info("Uploading file: {}, size: {}", file.getOriginalFilename(), file.getSize());
            File tempFile = File.createTempFile("upload_", ".pdf");
            file.transferTo(tempFile);
            logger.info("Temporary file created: {}", tempFile.getAbsolutePath());
            if (!tempFile.exists() || !tempFile.canRead()) {
                logger.error("Temporary file is not accessible: {}", tempFile.getAbsolutePath());
                return ResponseEntity.badRequest().body("Temporary file is not accessible");
            }
            Bulletin bulletin = pdfProcessingService.processPdf(tempFile, date);
            tempFile.delete();
            logger.info("Bulletin processed for date: {}", date);
            return ResponseEntity.ok("Bulletin processed for " + bulletin.getDate());
        } catch (IOException e) {
            logger.error("Error processing PDF: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error processing PDF: " + e.getMessage());
        }
    }
}