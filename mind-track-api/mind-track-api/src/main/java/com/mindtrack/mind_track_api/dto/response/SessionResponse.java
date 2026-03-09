package com.mindtrack.mind_track_api.dto.response;
import com.mindtrack.mind_track_api.entity.SessionStatus;
import com.mindtrack.mind_track_api.entity.SessionType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SessionResponse {
    private Long id;
    private Long patientId;
    private String patientFullName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String clinicalNotes;
    private SessionType type;
    private SessionStatus status;
}