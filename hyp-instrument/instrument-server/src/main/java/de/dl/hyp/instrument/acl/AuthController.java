package de.dl.hyp.instrument.acl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dl.hyp.instrument.api.model.GetTokenResponse;
import de.dl.hyp.instrument.api.resource.DefaultApi;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController implements DefaultApi {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  @Override
  public Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }

  @Override
  public ResponseEntity<GetTokenResponse> getToken(
      String grantType, String clientId, String clientSecret) {
    if ("client_credentials".equals(grantType)) {
      GetTokenResponse tokenResponse = jwtTokenProvider.generateToken(clientId);
      return ResponseEntity.ok(tokenResponse);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((GetTokenResponse) null);
    }
  }
}
