package de.dl.hyp.instrument.acl;

import de.dl.hyp.instrument.api.model.GetTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  public static final String USERNAME_CLAIM_KEY = "name";
  public static final String EMAIL_CLAIM_KEY = "email";

  public GetTokenResponse generateToken(String userNam) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + 10 * 1000);
    Header header = Jwts.header();
    header.setType("Bearer");
    JwtBuilder jwtBuilder =
        Jwts.builder()
            .setHeader((Map<String, Object>) header)
            .setExpiration(expirationDate)
            .claim(USERNAME_CLAIM_KEY, userNam)
            .claim(EMAIL_CLAIM_KEY, userNam + "@gmail.com")
            .setSubject(userNam)
            .setAudience("localhost");
    String compact = jwtBuilder.signWith(SignatureAlgorithm.HS256, this.getSigningKey()).compact();
    GetTokenResponse tokenResponse = new GetTokenResponse();
    tokenResponse.setTokenType("Bearer");
    tokenResponse.setAccessToken(compact);
    tokenResponse.setExpiresIn(2 * 1000);
    return tokenResponse;
  }

  public Claims verifyToken(String userToken, String expectedTyp) {
    Claims claims = this.checkSignature(userToken, expectedTyp);
    if (claims == null || this.isExpired(claims)) {
      throw new UnAuthenticatedException();
    }
    return claims;
  }

  private boolean isExpired(Claims claims) {
    Date now = new Date();
    Date expiration = claims.getExpiration();
    return expiration == null || expiration.before(now);
  }

  private Claims checkSignature(String jwt, String typ) {

    Jws<Claims> parseClaimsJws =
        Jwts.parser().setSigningKey(this.getSigningKey()).parseClaimsJws(jwt);
    JwsHeader header = parseClaimsJws.getHeader();
    if (header == null) {
      return null;
    }
    if (!"HS256".equalsIgnoreCase(header.getAlgorithm())) {
      return null;
    }
    if (typ != null && !typ.equalsIgnoreCase(header.getType())) {
      return null;
    }
    return parseClaimsJws.getBody();
  }

  private Key getSigningKey() {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    return new SecretKeySpec(
        "this.JITSI_MEET_TOKEN_SECRET".getBytes(StandardCharsets.UTF_8),
        signatureAlgorithm.getJcaName());
  }
}
