package com.mindtrack.mind_track_api.controller;
import com.mindtrack.mind_track_api.dto.request.CreateEmotionalRecordRequest;
import com.mindtrack.mind_track_api.dto.response.EmotionalRecordResponse;
import com.mindtrack.mind_track_api.service.EmotionalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emotional-records")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmotionalRecordController {
    private final EmotionalRecordService emotionalRecordService;

    @PostMapping
    public ResponseEntity<EmotionalRecordResponse> createRecord(@Valid @RequestBody CreateEmotionalRecordRequest req) {
        return ResponseEntity.ok(emotionalRecordService.createRecord(req));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmotionalRecordResponse>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(emotionalRecordService.getRecordsByPatient(patientId));
    }
}