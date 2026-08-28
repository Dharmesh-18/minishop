package com.dharmesh.minishop.order.consumer;

import com.dharmesh.minishop.common.config.KafkaTopicConfig;
import com.dharmesh.minishop.order.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventConsumer {

    @KafkaListener(topics = KafkaTopicConfig.ORDER_PLACED_TOPIC, groupId = "minishop-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent from Kafka: OrderId={}, ProductId={}, Total={}",
                event.getOrderId(), event.getProductId(), event.getTotalPrice());
    }
}
