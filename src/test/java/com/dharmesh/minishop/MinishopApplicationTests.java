package com.dharmesh.minishop;

import com.dharmesh.minishop.order.consumer.OrderEventConsumer;
import com.dharmesh.minishop.order.producer.OrderEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MinishopApplicationTests {

	@MockBean
	private OrderEventProducer orderEventProducer;

	@MockBean
	private OrderEventConsumer orderEventConsumer;

	@Test
	void contextLoads() {
	}

}
