package de.dl.hyp;

import de.dl.hyp.instrument.client.EnableCharismaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableCharismaClient
@SpringBootApplication
public class HypApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(HypApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(HypApplication.class);
  }
}
