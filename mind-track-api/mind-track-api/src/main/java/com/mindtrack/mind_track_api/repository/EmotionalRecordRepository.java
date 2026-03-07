package com.mindtrack.mind_track_api.repository;
import com.mindtrack.mind_track_api.entity.EmotionalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmotionalRecordRepository extends JpaRepository<EmotionalRecord, Long> {
    List<EmotionalRecord> findBySessionPatientIdOrderByRecordedAtAsc(Long patientId);
}