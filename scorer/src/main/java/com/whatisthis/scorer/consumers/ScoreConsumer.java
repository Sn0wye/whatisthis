package com.whatisthis.scorer.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whatisthis.scorer.repositories.ScoreRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import com.whatisthis.scorer.entities.Score;

@Component
public class ScoreConsumer {

    @Autowired
    private ScoreRepository repository;

    @RabbitListener(queues = "calculate-score")
    public void consume (String message) {
        try {
            ScoreEvent scoreEvent = new ScoreEvent(message);
            System.out.println("Received message: " + scoreEvent);

            Score score = new Score(scoreEvent);
            repository.save(score);
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing message: " + e.getMessage());
        }
    }
}
