package com.mindtrack.mind_track_api.service;
import com.mindtrack.mind_track_api.dto.request.CreateSessionRequest;
import com.mindtrack.mind_track_api.dto.response.SessionResponse;
import com.mindtrack.mind_track_api.entity.*;
import com.mindtrack.mind_track_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;

    private Psychologist getCurrentPsychologist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return psychologistRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Psychologist not found"));
    }

    public SessionResponse createSession(CreateSessionRequest req) {
        Psychologist psychologist = getCurrentPsychologist();
        Patient patient = patientRepository.findById(req.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        if (!patient.getPsychologist().getId().equals(psychologist.getId()))
            throw new RuntimeException("Access denied");
        Session session = Session.builder()
            .patient(patient).sessionDate(req.getSessionDate())
            .startTime(req.getStartTime()).endTime(req.getEndTime())
            .clinicalNotes(req.getClinicalNotes()).type(req.getType())
            .status(SessionStatus.SCHEDULED).build();
        Session saved = sessionRepository.save(session);
        return toResponse(saved);
    }

    public List<SessionResponse> getSessionsByPatient(Long patientId) {
        Psychologist psychologist = getCurrentPsychologist();
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        if (!patient.getPsychologist().getId().equals(psychologist.getId()))
            throw new RuntimeException("Access denied");
        return sessionRepository.findByPatientIdOrderBySessionDateAsc(patientId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SessionResponse updateSessionStatus(Long sessionId, SessionStatus newStatus) {
        Psychologist psychologist = getCurrentPsychologist();
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        if (!session.getPatient().getPsychologist().getId().equals(psychologist.getId()))
            throw new RuntimeException("Access denied");
        session.setStatus(newStatus);
        return toResponse(sessionRepository.save(session));
    }

    private SessionResponse toResponse(Session s) {
        return SessionResponse.builder().id(s.getId())
            .patientId(s.getPatient().getId())
            .patientFullName(s.getPatient().getFirstName() + " " + s.getPatient().getLastName())
            .sessionDate(s.getSessionDate()).startTime(s.getStartTime())
            .endTime(s.getEndTime()).clinicalNotes(s.getClinicalNotes())
            .type(s.getType()).status(s.getStatus()).build();
    }
}