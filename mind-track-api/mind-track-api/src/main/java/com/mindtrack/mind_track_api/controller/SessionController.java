package com.mindtrack.mind_track_api.controller;
import com.mindtrack.mind_track_api.dto.request.CreateSessionRequest;
import com.mindtrack.mind_track_api.dto.response.SessionResponse;
import com.mindtrack.mind_track_api.entity.SessionStatus;
import com.mindtrack.mind_track_api.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody CreateSessionRequest req) {
        return ResponseEntity.ok(sessionService.createSession(req));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SessionResponse>> getSessionsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(sessionService.getSessionsByPatient(patientId));
    }

    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<SessionResponse> updateStatus(@PathVariable Long sessionId, @RequestParam SessionStatus status) {
        return ResponseEntity.ok(sessionService.updateSessionStatus(sessionId, status));
    }
}