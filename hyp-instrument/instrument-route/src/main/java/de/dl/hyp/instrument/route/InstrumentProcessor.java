package de.dl.hyp.instrument.route;

import de.dl.hyp.instrument.api.model.LocalInstrument;
import de.dl.hyp.instrument.client.InstrumentApiFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class InstrumentProcessor {
  private final InstrumentApiFacade instrumentServiceFacade;

  public void process(Exchange exchange, @Body LocalInstrument instruments) {
    LocalInstrument instrument = instrumentServiceFacade.createInstrument(instruments);
    exchange.getIn().setBody(instrument);
  }
}
