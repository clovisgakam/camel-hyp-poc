package de.dl.hyp.instrument.client;

import com.auth0.jwt.JWT;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

@Configuration
public class FeignClientConfig {

  @Value("${charisma.url}")
  private String idpUrl;

  @Bean
  Feign.Builder feignBuilder() {
    return new Feign.Builder();
  }

  @Bean()
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  /** feign OAuth2ClientContext */
  @Bean
  public OAuth2ClientContext feignOAuth2ClientContext() {
    return new DefaultOAuth2ClientContext() {
      private boolean isExpired(Date expiration) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 1);
        Date now = instance.getTime();
        return expiration == null || expiration.before(now);
      }

      @Override
      public OAuth2AccessToken getAccessToken() {
        OAuth2AccessToken accessToken = super.getAccessToken();
        if (accessToken != null
            && OAuth2FeignRequestInterceptor.BEARER.equals(accessToken.getTokenType())) {
          String token = accessToken.getValue();
          if (isExpired(JWT.decode(token).getExpiresAt())) {
            setAccessToken(null);
          }
        }
        return super.getAccessToken();
      }
    };
  }

  @Bean
  public ErrorDecoder errorDecoder(OAuth2ClientContext feignOAuth2ClientContext) {
    return new RestClientErrorDecoder(feignOAuth2ClientContext);
  }

  @Bean
  public InstrumentDiscoveryClient discoveryClient(
      SimpleDiscoveryProperties simpleDiscoveryProperties) {
    return new InstrumentDiscoveryClient(simpleDiscoveryProperties);
  }

  @Bean
  public OAuth2FeignRequestInterceptor oAuthRequestInterceptor(
      OAuth2ClientContext feignOAuth2ClientContext) {
    return new OAuth2FeignRequestInterceptor(feignOAuth2ClientContext, oAuthResourceDetails());
  }

  public ClientCredentialsResourceDetails oAuthResourceDetails() {
    ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
    details.setClientId("admin");
    details.setClientSecret("password");
    details.setAccessTokenUri(idpUrl + "/oauth/token");
    return details;
  }

  @Slf4j
  public static class RestClientErrorDecoder implements ErrorDecoder {

    private final OAuth2ClientContext context;

    RestClientErrorDecoder(OAuth2ClientContext context) {
      this.context = context;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
      FeignException exception = FeignException.errorStatus(methodKey, response);
      int status = response.status();
      if (Arrays.asList(401).contains(status)) {
        log.error(
            "Received Feign response status : [{}]. access_token: [{}] Expired, reset access_token by null To be retrieved.",
            status,
            context.getAccessToken());
        context.setAccessToken(null);
        return new RetryableException(
            status,
            "Suspect access_token Expired, about to retry",
            response.request().httpMethod(),
            exception,
            new Date(),
            response.request());
      }
      return exception;
    }
  }
}
