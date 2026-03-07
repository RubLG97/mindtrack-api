package com.mindtrack.mind_track_api.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(nullable = false) private String firstName;
    @NotBlank @Column(nullable = false) private String lastName;
    @Email @Column(unique = true) private String email;
    @Column(nullable = false) private LocalDate birthDate;
    @Column(nullable = false) private LocalDate admissionDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PatientStatus status;
    @Column(length = 1000) private String notes;
    @ManyToOne @JoinColumn(name = "psychologist_id", nullable = false) private Psychologist psychologist;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL) private List<Session> sessions;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL) private List<Diagnosis> diagnoses;
}