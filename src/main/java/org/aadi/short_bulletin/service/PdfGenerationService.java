package org.aadi.short_bulletin.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.aadi.short_bulletin.entity.Bulletin;
import org.aadi.short_bulletin.entity.NewsItem;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(Bulletin bulletin) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("ShortBulletin for " + bulletin.getDate()));
        for (NewsItem item : bulletin.getNewsItems()) {
            document.add(new Paragraph(item.getTitle()).setBold());
            document.add(new Paragraph(item.getSummary()));
        }

        document.close();
        return baos.toByteArray();
    }
}