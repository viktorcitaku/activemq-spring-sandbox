package dev.viktorcitaku;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleListener {

  public void onMessage(String message) {
    log.info(message);
  }
}
