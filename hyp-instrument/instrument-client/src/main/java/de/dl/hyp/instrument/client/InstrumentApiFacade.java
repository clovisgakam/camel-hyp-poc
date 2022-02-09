package de.dl.hyp.instrument.client;

import de.dl.hyp.instrument.api.model.LocalInstrument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InstrumentApiFacade {
  private final InstrumentClient instrumentClient;

  private final AuthClient authClient;

  public LocalInstrument createInstrument(LocalInstrument instrument) {
    return instrumentClient.createInstrument(instrument).getBody();
  }
  // TODO: add other instrument method here
}
