package de.dl.hyp.instrument.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class BaseRestRouter extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    restConfiguration()
        .component("rest-openapi")
        .componentProperty("CamelJacksonEnableTypeConverter", "true")
        .componentProperty("CamelJacksonTypeConverterToPojo", "true")
        .dataFormatProperty("prettyPrint", "true")
        .bindingMode(RestBindingMode.json);
    // .setHost("{charisma.url}");
  }
}
