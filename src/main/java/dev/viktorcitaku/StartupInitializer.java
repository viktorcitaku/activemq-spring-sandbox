package dev.viktorcitaku;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.jms.MessageNotWriteableException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupInitializer implements ApplicationListener<ContextRefreshedEvent> {
  private static final String[] FIXED_MESSAGES =
      new String[] {"APPLE", "BANANA", "PINEAPPLE", "POMEGRENATE", "HONEYDEW"};
  private final JmsTemplate jmsTemplate;
  private final TaskExecutor taskExecutor;
  private boolean isDone;

  @Override
  @SneakyThrows
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Application is ready");
    if (!isDone) {
      Thread.sleep(TimeUnit.SECONDS.toMillis(5));
      taskExecutor.execute(
          () ->
              IntStream.range(1, 100_000)
                  .forEach(
                      i -> {
                        final var textMessage = new ActiveMQTextMessage();
                        try {
                          textMessage.setText(FIXED_MESSAGES[getRandomNumber()]);
                        } catch (MessageNotWriteableException e) {
                          throw new RuntimeException(e);
                        }
                        // textMessage.setGroupID();
                        jmsTemplate.convertAndSend("MY.QUEUE", textMessage);
                        try {
                          Thread.sleep(Duration.ofSeconds(10).toMillis());
                        } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                        }
                      }));
    }
    isDone = true;
  }

  private int getRandomNumber() {
    return (int) ((Math.random() * (5)) + 0);
  }
}
