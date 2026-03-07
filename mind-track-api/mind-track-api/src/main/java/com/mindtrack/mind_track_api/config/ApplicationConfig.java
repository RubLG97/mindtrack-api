package com.mindtrack.mind_track_api.config;
import com.mindtrack.mind_track_api.repository.PsychologistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final PsychologistRepository repo;
    @Bean public UserDetailsService userDetailsService() {
        return username -> repo.findByEmail(username)
            .map(p->User.withUsername(p.getEmail()).password(p.getPassword()).authorities("ROLE_PSYCHOLOGIST").build())
            .orElseThrow(()->new UsernameNotFoundException("Not found: "+username));
    }
    @Bean public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService()); p.setPasswordEncoder(passwordEncoder()); return p;
    }
    @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception { return c.getAuthenticationManager(); }
    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}