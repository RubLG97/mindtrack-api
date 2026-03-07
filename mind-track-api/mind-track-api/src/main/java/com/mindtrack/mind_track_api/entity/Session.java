package com.mindtrack.mind_track_api.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private LocalDate sessionDate;
    @Column(nullable = false) private LocalTime startTime;
    @Column(nullable = false) private LocalTime endTime;
    @NotBlank @Column(nullable = false, length = 5000) private String clinicalNotes;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SessionType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SessionStatus status;
    @ManyToOne @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL) private EmotionalRecord emotionalRecord;
}