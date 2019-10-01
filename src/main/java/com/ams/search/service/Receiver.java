package com.ams.search.service;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Receiver
{
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	private final SearchService searchService;

	@Bean
	Queue queue()
	{
		return new Queue("SearchQ", false);
	}

	@RabbitListener(queues = "SearchQ")
	public void processMessage(Map<String, Object> message)
	{
		logger.info(">> Received message event, updating Inventory");
		//call repository and update the fare for the given flight
		searchService
			.updateInventory((String) message.get("FLIGHT_NUMBER"), (LocalDate) message
				.get("FLIGHT_DATE"), (int) message.get("NEW_INVENTORY"));

	}
}