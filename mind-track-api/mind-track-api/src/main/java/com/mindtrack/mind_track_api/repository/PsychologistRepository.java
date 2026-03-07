package com.mindtrack.mind_track_api.repository;
import com.mindtrack.mind_track_api.entity.Psychologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologist, Long> {
    Optional<Psychologist> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByLicenseNumber(String licenseNumber);
}