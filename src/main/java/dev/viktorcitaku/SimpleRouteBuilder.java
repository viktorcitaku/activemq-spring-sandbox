package dev.viktorcitaku;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.processor.idempotent.RedisStringIdempotentRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleRouteBuilder extends RouteBuilder {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void configure() {
    final var redis = new RedisStringIdempotentRepository(redisTemplate, "redis");
    redis.setExpiry(Duration.ofSeconds(30).toSeconds());

    from("activemq:queue:MY.QUEUE?concurrentConsumers=2")
        .idempotentConsumer(body(), redis)
        .bean(SimpleListener.class, "onMessage");
  }
}
