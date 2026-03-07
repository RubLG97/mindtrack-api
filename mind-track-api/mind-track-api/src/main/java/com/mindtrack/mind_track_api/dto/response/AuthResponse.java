package com.mindtrack.mind_track_api.dto.response;
import lombok.*;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
}