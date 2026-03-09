package com.mindtrack.mind_track_api.controller;
import com.mindtrack.mind_track_api.dto.response.EmotionalRecordResponse;
import com.mindtrack.mind_track_api.dto.response.PatientResponse;
import com.mindtrack.mind_track_api.dto.response.SessionResponse;
import com.mindtrack.mind_track_api.service.EmotionalRecordService;
import com.mindtrack.mind_track_api.service.PatientService;
import com.mindtrack.mind_track_api.service.PdfReportService;
import com.mindtrack.mind_track_api.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {
    private final PdfReportService pdfReportService;
    private final PatientService patientService;
    private final SessionService sessionService;
    private final EmotionalRecordService emotionalRecordService;

    @GetMapping("/patient/{patientId}/pdf")
    public ResponseEntity<byte[]> generatePatientReport(@PathVariable Long patientId) {
        PatientResponse patient = patientService.getPatientById(patientId);
        List<SessionResponse> sessions = sessionService.getSessionsByPatient(patientId);
        List<EmotionalRecordResponse> records = emotionalRecordService.getRecordsByPatient(patientId);
        byte[] pdf = pdfReportService.generatePatientReport(patient, sessions, records);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe-paciente-" + patientId + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}