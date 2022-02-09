package de.dl.hyp.instrument.route;

import de.dl.hyp.instrument.api.model.Instruments;
import de.dl.hyp.instrument.api.model.LocalInstrument;

public class InstrumentMapper {

  public LocalInstrument mapToLocal(Instruments instruments) {
    LocalInstrument localInstrument = new LocalInstrument();
    localInstrument.setRole(instruments.getType());
    localInstrument.setId(instruments.getId());
    localInstrument.setCurrency(instruments.getCurrency());
    localInstrument.setPrice(instruments.getPrice() * 2345);
    return localInstrument;
  }
}
