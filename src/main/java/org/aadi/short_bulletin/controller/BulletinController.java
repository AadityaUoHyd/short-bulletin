package org.aadi.short_bulletin.controller;

import org.aadi.short_bulletin.entity.Bulletin;
import org.aadi.short_bulletin.repository.BulletinRepository;
import org.aadi.short_bulletin.service.PdfGenerationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bulletin")
public class BulletinController {

    private final BulletinRepository bulletinRepository;
    private final PdfGenerationService pdfGenerationService;

    public BulletinController(BulletinRepository bulletinRepository, PdfGenerationService pdfGenerationService) {
        this.bulletinRepository = bulletinRepository;
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping("/dates")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<LocalDate> getBulletinDates() {
        return bulletinRepository.findAllDates(); // Assume custom query method
    }

    @GetMapping("/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Bulletin getBulletinByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bulletinRepository.findByDate(date).orElseThrow();
    }

    @GetMapping("/download/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Bulletin bulletin = bulletinRepository.findByDate(date).orElseThrow();
        byte[] pdfBytes = pdfGenerationService.generatePdf(bulletin);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ShortBulletin_" + date + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}