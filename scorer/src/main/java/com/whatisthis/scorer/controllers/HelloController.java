package com.whatisthis.scorer.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatisthis.scorer.consumers.ScoreEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class HelloController {
    @Autowired private RabbitTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/hello")
    public String hello() throws JsonProcessingException {

        this.template.convertAndSend(
                "amq.direct",
                "calculate-score-routing-key",
                objectMapper.writeValueAsString(
                        new ScoreEvent(
                                "123123",
                                1000000,
                                40000,
                                500000
                        )
                )
        );

        return "Hello, world!";
    }
}