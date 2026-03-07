package com.mindtrack.mind_track_api.repository;
import com.mindtrack.mind_track_api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByPatientId(Long patientId);
    List<Session> findByPatientIdOrderBySessionDateAsc(Long patientId);
}