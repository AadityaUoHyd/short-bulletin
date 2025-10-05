package org.aadi.short_bulletin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aadi.short_bulletin.dto.NewsSummaryDto;
import org.aadi.short_bulletin.entity.Bulletin;
import org.aadi.short_bulletin.entity.NewsItem;
import org.aadi.short_bulletin.repository.BulletinRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(PdfProcessingService.class);
    private final LlmService llmService;
    private final BulletinRepository bulletinRepository;
    private final ObjectMapper objectMapper;

    public PdfProcessingService(LlmService llmService, BulletinRepository bulletinRepository, ObjectMapper objectMapper) {
        this.llmService = llmService;
        this.bulletinRepository = bulletinRepository;
        this.objectMapper = objectMapper;
    }

    public Bulletin processPdf(File pdfFile, LocalDate date) throws IOException {
        logger.info("Processing PDF file: {}", pdfFile.getAbsolutePath());
        if (!pdfFile.exists() || !pdfFile.canRead()) {
            logger.error("PDF file does not exist or is not readable: {}", pdfFile.getAbsolutePath());
            throw new IOException("PDF file does not exist or is not readable: " + pdfFile.getAbsolutePath());
        }

        String text = extractText(pdfFile);
        String summariesJson = llmService.generateSummary(text);
        List<NewsSummaryDto> summaries = parseSummaries(summariesJson);

        Bulletin bulletin = new Bulletin();
        bulletin.setDate(date);
        List<NewsItem> newsItems = summaries.stream().map(dto -> {
            NewsItem item = new NewsItem();
            item.setTitle(dto.getTitle());
            item.setSummary(dto.getSummary());
            item.setBulletin(bulletin);
            return item;
        }).collect(Collectors.toList());
        bulletin.setNewsItems(newsItems);

        logger.info("Saving bulletin for date: {}", date);
        return bulletinRepository.save(bulletin);
    }

    private String extractText(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            if (document.isEncrypted()) {
                logger.error("PDF is encrypted and not supported");
                throw new IOException("Encrypted PDFs are not supported");
            }
            logger.info("PDF loaded successfully, page count: {}", document.getNumberOfPages());
            PDFTextStripper stripper = new PDFTextStripper();
            StringBuilder text = new StringBuilder();
            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String pageText = stripper.getText(document);
                if (!pageText.trim().isEmpty()) {
                    text.append(pageText).append("\n");
                }
            }
            String result = text.toString();
            if (result.trim().isEmpty()) {
                logger.error("No text extracted from PDF");
                throw new IOException("No text extracted from PDF");
            }
            logger.info("Extracted text length: {}", result.length());
            return result;
        } catch (IOException e) {
            logger.error("Failed to load PDF: {}", e.getMessage(), e);
            throw new IOException("Failed to load PDF: " + e.getMessage(), e);
        }
    }

    private List<NewsSummaryDto> parseSummaries(String json) throws IOException {
        try {
            logger.info("Parsing LLM JSON response");
            return objectMapper.readValue(json, new TypeReference<List<NewsSummaryDto>>() {});
        } catch (IOException e) {
            logger.error("Failed to parse LLM response: {}", e.getMessage(), e);
            throw new IOException("Failed to parse LLM response: " + e.getMessage(), e);
        }
    }
}