package com.mindtrack.mind_track_api.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EmotionalRecordResponse {
    private Long id;
    private Long sessionId;
    private String patientFullName;
    private Integer anxietyLevel;
    private Integer moodLevel;
    private Integer sleepQuality;
    private Integer socialFunctioning;
    private Integer motivationLevel;
    private String observations;
    private LocalDateTime recordedAt;
}