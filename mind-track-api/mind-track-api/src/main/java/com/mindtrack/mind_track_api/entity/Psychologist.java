package com.mindtrack.mind_track_api.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "psychologists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Psychologist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(nullable = false) private String firstName;
    @NotBlank @Column(nullable = false) private String lastName;
    @Email @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String password;
    @Column(unique = true) private String licenseNumber;
    @OneToMany(mappedBy = "psychologist", cascade = CascadeType.ALL)
    private List<Patient> patients;
}