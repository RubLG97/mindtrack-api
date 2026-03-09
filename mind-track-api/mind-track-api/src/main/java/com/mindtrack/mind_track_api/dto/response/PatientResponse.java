package com.mindtrack.mind_track_api.dto.response;
import com.mindtrack.mind_track_api.entity.PatientStatus;
import lombok.*;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private LocalDate admissionDate;
    private PatientStatus status;
    private String notes;
}