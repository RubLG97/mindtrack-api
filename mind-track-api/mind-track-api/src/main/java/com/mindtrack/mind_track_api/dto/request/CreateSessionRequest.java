package com.mindtrack.mind_track_api.dto.request;
import com.mindtrack.mind_track_api.entity.SessionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSessionRequest {
    @NotNull private Long patientId;
    @NotNull private LocalDate sessionDate;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    @NotBlank private String clinicalNotes;
    @NotNull private SessionType type;
}