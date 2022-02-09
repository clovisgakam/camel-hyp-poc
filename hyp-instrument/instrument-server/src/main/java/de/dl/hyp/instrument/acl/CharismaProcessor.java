package de.dl.hyp.instrument.acl;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CharismaProcessor {

  @Autowired private ProducerTemplate producerTemplate;

  @Autowired private ConsumerTemplate consumerTemplate;

  public void processNextInvoice() {
    String receiveBody =
        consumerTemplate.receiveBody("timer:hello?period={{myPeriod}}", String.class);
    producerTemplate.sendBody(receiveBody + "invoice.id()");
  }
}
