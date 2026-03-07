package com.mindtrack.mind_track_api.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotional_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmotionalRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(1) @Max(10) @Column(nullable = false) private Integer anxietyLevel;
    @Min(1) @Max(10) @Column(nullable = false) private Integer moodLevel;
    @Min(1) @Max(10) @Column(nullable = false) private Integer sleepQuality;
    @Min(1) @Max(10) @Column(nullable = false) private Integer socialFunctioning;
    @Min(1) @Max(10) @Column(nullable = false) private Integer motivationLevel;
    @Column(length = 1000) private String observations;
    @Column(nullable = false) private LocalDateTime recordedAt;
    @OneToOne @JoinColumn(name = "session_id", nullable = false) private Session session;
}