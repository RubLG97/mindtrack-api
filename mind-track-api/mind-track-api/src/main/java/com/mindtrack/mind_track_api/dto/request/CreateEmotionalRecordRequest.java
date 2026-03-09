package com.mindtrack.mind_track_api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateEmotionalRecordRequest {
    @NotNull private Long sessionId;
    @NotNull @Min(1) @Max(10) private Integer anxietyLevel;
    @NotNull @Min(1) @Max(10) private Integer moodLevel;
    @NotNull @Min(1) @Max(10) private Integer sleepQuality;
    @NotNull @Min(1) @Max(10) private Integer socialFunctioning;
    @NotNull @Min(1) @Max(10) private Integer motivationLevel;
    private String observations;
    @NotNull private LocalDateTime recordedAt;
}