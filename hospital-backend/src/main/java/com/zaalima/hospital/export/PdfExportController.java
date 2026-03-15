package com.zaalima.hospital.export;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.zaalima.hospital.audit.LogAccess;
import com.zaalima.hospital.patient.Patient;
import com.zaalima.hospital.patient.PatientRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PdfExportController {

    private final PatientRepository patientRepository;

    @GetMapping("/patients/{id}/history")
    @LogAccess(entity = "PatientHistory", action = "EXPORT_PDF")
    public void exportPatientHistory(@PathVariable Long id,
                                     @RequestParam String password,
                                     HttpServletResponse response) throws Exception {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

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
        document.add(new Paragraph("MedNex Patient Medical Record"));
        document.add(new Paragraph("-----------------------------"));
        document.add(new Paragraph("Name: " + patient.getFirstName() + " " + patient.getLastName()));
        document.add(new Paragraph("DOB: " + patient.getDob()));
        document.add(new Paragraph("Gender: " + patient.getGender()));
        document.add(new Paragraph("Contact: " + patient.getContactNumber()));
        document.add(new Paragraph("Email: " + patient.getEmail()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Admission Details:"));
        document.add(new Paragraph(patient.getAdmissionDetails() != null ? patient.getAdmissionDetails().toString() : "N/A"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Medical History:"));
        document.add(new Paragraph(patient.getMedicalHistory() != null ? patient.getMedicalHistory().toString() : "N/A"));
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=history_" + id + ".pdf");
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.getOutputStream().write(pdfBytes);
    }
}
