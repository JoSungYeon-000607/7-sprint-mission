package com.sprint.mission.discodeit.config.jwt;

import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private final Key key;
  private final long accessTokenValidityInMilliseconds;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
  }

  public String createAccessToken(AuthUserDTO authUser) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + this.accessTokenValidityInMilliseconds);

    return Jwts.builder()
        .setSubject(authUser.authUserId().toString())
        .claim("username", authUser.username())
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
