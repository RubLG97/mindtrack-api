package com.mindtrack.mind_track_api.service;
import com.mindtrack.mind_track_api.dto.request.CreatePatientRequest;
import com.mindtrack.mind_track_api.dto.response.PatientResponse;
import com.mindtrack.mind_track_api.entity.*;
import com.mindtrack.mind_track_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;

    private Psychologist getCurrentPsychologist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return psychologistRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Psychologist not found"));
    }

    public PatientResponse createPatient(CreatePatientRequest req) {
        Psychologist psychologist = getCurrentPsychologist();
        Patient patient = Patient.builder()
            .firstName(req.getFirstName()).lastName(req.getLastName())
            .email(req.getEmail()).birthDate(req.getBirthDate())
            .admissionDate(req.getAdmissionDate()).notes(req.getNotes())
            .status(PatientStatus.ACTIVE).psychologist(psychologist).build();
        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    public List<PatientResponse> getMyPatients() {
        Psychologist psychologist = getCurrentPsychologist();
        return patientRepository.findByPsychologistId(psychologist.getId())
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PatientResponse getPatientById(Long id) {
        Psychologist psychologist = getCurrentPsychologist();
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        if (!patient.getPsychologist().getId().equals(psychologist.getId()))
            throw new RuntimeException("Access denied");
        return toResponse(patient);
    }

    private PatientResponse toResponse(Patient p) {
        return PatientResponse.builder().id(p.getId()).firstName(p.getFirstName())
            .lastName(p.getLastName()).email(p.getEmail()).birthDate(p.getBirthDate())
            .admissionDate(p.getAdmissionDate()).status(p.getStatus()).notes(p.getNotes()).build();
    }
}