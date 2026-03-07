package com.mindtrack.mind_track_api.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "diagnoses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Diagnosis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String cie10Code;
    @Column(nullable = false) private String description;
    @Column(nullable = false) private LocalDate diagnosisDate;
    @Column(nullable = false) private Boolean isPrimary;
    @ManyToOne @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
}