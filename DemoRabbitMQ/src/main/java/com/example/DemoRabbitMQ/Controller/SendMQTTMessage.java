package com.example.DemoRabbitMQ.Controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class SendMQTTMessage {
    private final RabbitTemplate rabbitTemplate;

    public SendMQTTMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public String sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend("amq.topic", "suara-routing-key", message);
        return "Message sent: " + message;
    }
}
