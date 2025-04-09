package com.SuaraCloud.DemoService.controller;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/demo")
public class DemoController {
    private final RabbitTemplate rabbitTemplate;

    public DemoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> getAnonymous() {
        return ResponseEntity.ok("Welcome to GeeksforGeeks");
    }

    @PostMapping("/mqtt")
    public String sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend("amq.topic", "suara-routing-key", message);
        return "Message sent: " + message;
    }
}
