package com.mindtrack.mind_track_api.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.secret}") private String secretKey;
    @Value("${app.jwt.expiration}") private long jwtExpiration;
    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public String generateToken(UserDetails u) { return generateToken(new HashMap<>(), u); }
    public String generateToken(Map<String,Object> extra, UserDetails u) {
        return Jwts.builder().setClaims(extra).setSubject(u.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
    public boolean isTokenValid(String token, UserDetails u) { return extractUsername(token).equals(u.getUsername()) && !isTokenExpired(token); }
    private boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); }
    private Date extractExpiration(String t) { return extractClaim(t, Claims::getExpiration); }
    public <T> T extractClaim(String t, Function<Claims,T> r) { return r.apply(extractAllClaims(t)); }
    private Claims extractAllClaims(String t) { return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(t).getBody(); }
    private Key getSignInKey() { return Keys.hmacShaKeyFor(secretKey.getBytes()); }
}