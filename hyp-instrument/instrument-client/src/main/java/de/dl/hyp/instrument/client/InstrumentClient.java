package de.dl.hyp.instrument.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dl.hyp.instrument.api.resource.InstrumentsApi;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "instrumentClient")
public interface InstrumentClient extends InstrumentsApi {
  default Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  default Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }
}
