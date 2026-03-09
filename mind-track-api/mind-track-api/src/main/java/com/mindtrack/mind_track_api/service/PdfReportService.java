package com.mindtrack.mind_track_api.service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mindtrack.mind_track_api.dto.response.EmotionalRecordResponse;
import com.mindtrack.mind_track_api.dto.response.PatientResponse;
import com.mindtrack.mind_track_api.dto.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfReportService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(60, 100, 170));
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font VALUE_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);

    public byte[] generatePatientReport(PatientResponse patient, List<SessionResponse> sessions, List<EmotionalRecordResponse> records) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 50, 50, 60, 60);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Cabecera
            Paragraph title = new Paragraph("INFORME CLÍNICO DE EVOLUCIÓN", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("MindTrack — Sistema de Seguimiento Psicológico", SMALL_FONT));
            doc.add(Chunk.NEWLINE);
            addSeparator(doc);

            // Datos del paciente
            doc.add(new Paragraph("DATOS DEL PACIENTE", SUBTITLE_FONT));
            doc.add(Chunk.NEWLINE);
            addField(doc, "Nombre completo: ", patient.getFirstName() + " " + patient.getLastName());
            addField(doc, "Email: ", patient.getEmail() != null ? patient.getEmail() : "-");
            addField(doc, "Fecha de nacimiento: ", patient.getBirthDate().toString());
            addField(doc, "Fecha de admisión: ", patient.getAdmissionDate().toString());
            addField(doc, "Estado: ", patient.getStatus().toString());
            if (patient.getNotes() != null) addField(doc, "Notas: ", patient.getNotes());
            doc.add(Chunk.NEWLINE);
            addSeparator(doc);

            // Resumen emocional
            if (!records.isEmpty()) {
                doc.add(new Paragraph("RESUMEN DE EVOLUCIÓN EMOCIONAL", SUBTITLE_FONT));
                doc.add(Chunk.NEWLINE);
                double avgAnxiety = records.stream().mapToInt(EmotionalRecordResponse::getAnxietyLevel).average().orElse(0);
                double avgMood = records.stream().mapToInt(EmotionalRecordResponse::getMoodLevel).average().orElse(0);
                double avgSleep = records.stream().mapToInt(EmotionalRecordResponse::getSleepQuality).average().orElse(0);
                double avgMotivation = records.stream().mapToInt(EmotionalRecordResponse::getMotivationLevel).average().orElse(0);
                addField(doc, "Ansiedad media: ", String.format("%.1f / 10", avgAnxiety));
                addField(doc, "Estado de ánimo medio: ", String.format("%.1f / 10", avgMood));
                addField(doc, "Calidad del sueño media: ", String.format("%.1f / 10", avgSleep));
                addField(doc, "Motivación media: ", String.format("%.1f / 10", avgMotivation));
                addField(doc, "Total de registros: ", String.valueOf(records.size()));
                doc.add(Chunk.NEWLINE);
                addSeparator(doc);
            }

            // Historial de sesiones
            if (!sessions.isEmpty()) {
                doc.add(new Paragraph("HISTORIAL DE SESIONES", SUBTITLE_FONT));
                doc.add(Chunk.NEWLINE);
                for (SessionResponse s : sessions) {
                    Paragraph sessionTitle = new Paragraph(
                        s.getSessionDate() + "  |  " + s.getType() + "  |  " + s.getStatus(), LABEL_FONT);
                    doc.add(sessionTitle);
                    addField(doc, "Hora: ", s.getStartTime() + " - " + s.getEndTime());
                    addField(doc, "Notas clínicas: ", s.getClinicalNotes());
                    doc.add(Chunk.NEWLINE);
                }
                addSeparator(doc);
            }

            // Registros emocionales detallados
            if (!records.isEmpty()) {
                doc.add(new Paragraph("REGISTROS EMOCIONALES DETALLADOS", SUBTITLE_FONT));
                doc.add(Chunk.NEWLINE);
                for (EmotionalRecordResponse r : records) {
                    addField(doc, "Fecha: ", r.getRecordedAt().toString());
                    addField(doc, "Ansiedad: ", r.getAnxietyLevel() + "/10");
                    addField(doc, "Ánimo: ", r.getMoodLevel() + "/10");
                    addField(doc, "Sueño: ", r.getSleepQuality() + "/10");
                    addField(doc, "Funcionamiento social: ", r.getSocialFunctioning() + "/10");
                    addField(doc, "Motivación: ", r.getMotivationLevel() + "/10");
                    if (r.getObservations() != null) addField(doc, "Observaciones: ", r.getObservations());
                    doc.add(Chunk.NEWLINE);
                }
            }

            doc.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Documento generado automáticamente por MindTrack. Confidencial.", SMALL_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private void addField(Document doc, String label, String value) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label, LABEL_FONT));
        p.add(new Chunk(value, VALUE_FONT));
        doc.add(p);
    }

    private void addSeparator(Document doc) throws DocumentException {
        LineSeparator line = new LineSeparator();
        line.setLineColor(new BaseColor(200, 200, 200));
        doc.add(new Chunk(line));
        doc.add(Chunk.NEWLINE);
    }
}