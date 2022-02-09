package de.dl.hyp.instrument.acl;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class IdentityTokenFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
      throws ServletException, IOException {
    String identityToken = this.resolveIdentityTokenFromRequest(req);
    Optional<Authentication> authentication = this.getAuthentication(identityToken);
    if (authentication.isPresent()) {
      SecurityContextHolder.getContext().setAuthentication(authentication.get());
      filterChain.doFilter(req, res);
    } else {
      res.sendError(401);
      return;
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getRequestURI().equals("/oauth/token");
  }

  private String resolveIdentityTokenFromRequest(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    return Objects.nonNull(authorization)
        ? StringUtils.substringAfter(authorization, "Bearer ")
        : null;
  }

  private Optional<Authentication> getAuthentication(String identityToken) {
    try {
      Claims identityClaims = this.jwtTokenProvider.verifyToken(identityToken, "Bearer");
      if (Objects.nonNull(identityClaims)) {
        return Optional.of(new UsernamePasswordAuthenticationToken("admin", "password"));
      }
    } catch (Exception e) {
      this.logger.error(e.getMessage());
    }
    return Optional.empty();
  }
}
