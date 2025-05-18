package com.SuaraCloud.SongService.RabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	public Runner( RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		// Without this the queue isn't binded to the routing key for some reason
		 System.out.println("Sending message...");
		 rabbitTemplate.convertAndSend("amq.topic", "suara-routing-key", "Hello from song-service!");
	}
}
