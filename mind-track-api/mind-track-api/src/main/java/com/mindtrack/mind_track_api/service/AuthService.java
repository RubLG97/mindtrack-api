package com.mindtrack.mind_track_api.service;
import com.mindtrack.mind_track_api.dto.request.*;
import com.mindtrack.mind_track_api.dto.response.AuthResponse;
import com.mindtrack.mind_track_api.entity.Psychologist;
import com.mindtrack.mind_track_api.repository.PsychologistRepository;
import com.mindtrack.mind_track_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PsychologistRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.getEmail())) throw new RuntimeException("Email already registered");
        Psychologist p = Psychologist.builder().firstName(req.getFirstName()).lastName(req.getLastName())
            .email(req.getEmail()).password(encoder.encode(req.getPassword())).licenseNumber(req.getLicenseNumber()).build();
        repo.save(p);
        var u = User.withUsername(p.getEmail()).password(p.getPassword()).authorities("ROLE_PSYCHOLOGIST").build();
        return AuthResponse.builder().token(jwtService.generateToken(u)).email(p.getEmail()).firstName(p.getFirstName()).lastName(p.getLastName()).build();
    }
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Psychologist p = repo.findByEmail(req.getEmail()).orElseThrow(()->new RuntimeException("Not found"));
        var u = User.withUsername(p.getEmail()).password(p.getPassword()).authorities("ROLE_PSYCHOLOGIST").build();
        return AuthResponse.builder().token(jwtService.generateToken(u)).email(p.getEmail()).firstName(p.getFirstName()).lastName(p.getLastName()).build();
    }
}