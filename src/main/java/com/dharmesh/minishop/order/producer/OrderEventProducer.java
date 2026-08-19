package com.dharmesh.minishop.order.producer;

import com.dharmesh.minishop.common.config.KafkaTopicConfig;
import com.dharmesh.minishop.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderPlaceEvent(OrderPlacedEvent event) {
        log.info("Publishing OrderPlacedEvent for orderId: {}", event.getOrderId());
        kafkaTemplate.send(KafkaTopicConfig.ORDER_PLACED_TOPIC, event.getOrderId(), event);
    }
}
