package de.dl.hyp.instrument.acl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dl.hyp.instrument.api.model.Instruments;
import de.dl.hyp.instrument.api.model.LocalInstrument;
import de.dl.hyp.instrument.api.resource.InstrumentsApi;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstrumentController implements InstrumentsApi {
  private final ConcurrentMap<String, LocalInstrument> instrumentsMap = new ConcurrentHashMap<>();

  @Override
  public Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  @Override
  public Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }

  @Override
  public ResponseEntity<LocalInstrument> createInstrument(LocalInstrument body) {
    body.setId(UUID.randomUUID().toString());
    instrumentsMap.putIfAbsent(body.getId(), body);
    return ResponseEntity.ok(body);
  }

  @Override
  public ResponseEntity<Instruments> findInstrumentByID(String id) {
    return ResponseEntity.status(200).body(null);
  }

  @Override
  public ResponseEntity<List<Instruments>> findInstruments() {
    return ResponseEntity.status(200).body(null);
  }

  @PostConstruct
  public void postConstruct() {
    Random random = new Random();
    for (int i = 0; i < 50; i++) {
      LocalInstrument instruments = new LocalInstrument();
      instruments.setId(UUID.randomUUID().toString());
      instruments.setPrice(random.nextInt());
      instruments.setCurrency(Currency.getInstance("EUR").getCurrencyCode());
      instruments.setRole(instruments.getId());
      instrumentsMap.putIfAbsent(instruments.getId(), instruments);
    }
  }
}
