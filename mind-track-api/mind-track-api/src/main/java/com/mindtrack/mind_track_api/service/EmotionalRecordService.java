package com.mindtrack.mind_track_api.service;
import com.mindtrack.mind_track_api.dto.request.CreateEmotionalRecordRequest;
import com.mindtrack.mind_track_api.dto.response.EmotionalRecordResponse;
import com.mindtrack.mind_track_api.entity.*;
import com.mindtrack.mind_track_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmotionalRecordService {
    private final EmotionalRecordRepository emotionalRecordRepository;
    private final SessionRepository sessionRepository;
    private final PsychologistRepository psychologistRepository;

    private Psychologist getCurrentPsychologist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return psychologistRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Psychologist not found"));
    }

    public EmotionalRecordResponse createRecord(CreateEmotionalRecordRequest req) {
        Psychologist psychologist = getCurrentPsychologist();
        Session session = sessionRepository.findById(req.getSessionId())
            .orElseThrow(() -> new RuntimeException("Session not found"));
        if (!session.getPatient().getPsychologist().getId().equals(psychologist.getId()))
            throw new RuntimeException("Access denied");
        EmotionalRecord record = EmotionalRecord.builder()
            .session(session).anxietyLevel(req.getAnxietyLevel())
            .moodLevel(req.getMoodLevel()).sleepQuality(req.getSleepQuality())
            .socialFunctioning(req.getSocialFunctioning())
            .motivationLevel(req.getMotivationLevel())
            .observations(req.getObservations()).recordedAt(req.getRecordedAt()).build();
        EmotionalRecord saved = emotionalRecordRepository.save(record);
        return toResponse(saved);
    }

    public List<EmotionalRecordResponse> getRecordsByPatient(Long patientId) {
        Psychologist psychologist = getCurrentPsychologist();
        return emotionalRecordRepository.findBySessionPatientIdOrderByRecordedAtAsc(patientId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private EmotionalRecordResponse toResponse(EmotionalRecord r) {
        return EmotionalRecordResponse.builder().id(r.getId())
            .sessionId(r.getSession().getId())
            .patientFullName(r.getSession().getPatient().getFirstName() + " " + r.getSession().getPatient().getLastName())
            .anxietyLevel(r.getAnxietyLevel()).moodLevel(r.getMoodLevel())
            .sleepQuality(r.getSleepQuality()).socialFunctioning(r.getSocialFunctioning())
            .motivationLevel(r.getMotivationLevel()).observations(r.getObservations())
            .recordedAt(r.getRecordedAt()).build();
    }
}