package de.dl.hyp.instrument.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;

@Slf4j
public class InstrumentDiscoveryClient extends SimpleDiscoveryClient {
  private final List<String> servers = Arrays.asList("http://localhost:80", "http://localhost:81");
  int j = 3;

  public InstrumentDiscoveryClient(SimpleDiscoveryProperties simpleDiscoveryProperties) {
    super(simpleDiscoveryProperties);
  }

  @Override
  public List<ServiceInstance> getInstances(String serviceId) {
    List<ServiceInstance> serviceInstances = new ArrayList<>();
    try {
      URI url = new URI(servers.get(j % 2));
      j++;
      DefaultServiceInstance https = new DefaultServiceInstance();
      https.setUri(url);
      https.setInstanceId(serviceId);
      https.setInstanceId(UUID.randomUUID().toString());

      new DefaultServiceInstance(
          UUID.randomUUID().toString(),
          serviceId,
          url.getHost(),
          url.getPort(),
          url.getScheme().equals("https"));
      serviceInstances.add(https);
      log.info("HOST: {}", https);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }

    return serviceInstances;
  }
}
