package com.mindtrack.mind_track_api.repository;
import com.mindtrack.mind_track_api.entity.Patient;
import com.mindtrack.mind_track_api.entity.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByPsychologistId(Long psychologistId);
    List<Patient> findByPsychologistIdAndStatus(Long psychologistId, PatientStatus status);
    Boolean existsByEmail(String email);
}