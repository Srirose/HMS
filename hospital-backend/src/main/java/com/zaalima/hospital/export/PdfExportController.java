package com.zaalima.hospital.export;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.zaalima.hospital.audit.LogAccess;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/export")
public class PdfExportController {

    @GetMapping("/patients/{id}/history")
    @LogAccess(entity = "PatientHistory", action = "EXPORT_PDF")
    public void exportPatientHistory(@PathVariable Long id,
                                     @RequestParam String password,
                                     HttpServletResponse response) throws Exception {

        // Simple demo PDF content – replace with real patient/admission data lookup.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setEncryption(
                password.getBytes(),
                password.getBytes(),
                PdfWriter.ALLOW_PRINTING,
                PdfWriter.STANDARD_ENCRYPTION_128
        );

        document.open();
        document.add(new Paragraph("Patient History for ID: " + id));
        document.add(new Paragraph("This is a demo PDF. Replace with real history content."));
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=history_" + id + ".pdf");
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.getOutputStream().write(pdfBytes);
    }
}


