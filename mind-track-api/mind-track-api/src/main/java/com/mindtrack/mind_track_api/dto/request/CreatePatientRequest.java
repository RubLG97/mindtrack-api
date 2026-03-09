package com.mindtrack.mind_track_api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreatePatientRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    private String email;
    @NotNull private LocalDate birthDate;
    @NotNull private LocalDate admissionDate;
    private String notes;
}