package de.dl.hyp.instrument.route;

import de.dl.hyp.instrument.api.model.Instruments;
import lombok.RequiredArgsConstructor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 *
 * <p>Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@RequiredArgsConstructor
@Component
public class InstrumentToStreamRouter extends BaseRestRouter {
  private final InstrumentProcessor instrumentProcessor;

  @Override
  public void configure() throws Exception {

    super.configure();
    from("timer:instrument?period={{inPeriod}}")
        .routeId("instrument")
        .to("rest-openapi:classpath:instrument-api.json#FindInstruments")
        .unmarshal()
        .json(JsonLibrary.Gson, Instruments[].class)
        .split()
        .body(Instruments.class)
        .log("${body}")
        .bean(InstrumentMapper.class, "mapToLocal")
        .bean(instrumentProcessor, "process")
        .log("${body}");
  }
}
