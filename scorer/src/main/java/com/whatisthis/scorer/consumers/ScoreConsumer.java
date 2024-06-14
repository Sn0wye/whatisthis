package com.whatisthis.scorer.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whatisthis.scorer.constants.RabbitMQConstants;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.whatisthis.scorer.entities.Score;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ScoreConsumer {

    @Autowired
    private ScoreRepository repository;

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConstants.CALCULATE_SCORE_QUEUE)
    public void consume(String message) {
        try {
            ScoreEvent scoreEvent = new ScoreEvent(message);
            System.out.println("Received message: " + scoreEvent);

            Score score = new Score(scoreEvent);
            int creditScore = calculateCreditScoreService.execute(
                    score.getIncome(),
                    score.getDebt(),
                    score.getPropertyValue()
            );

            score.setCreditScore(creditScore);

            repository.save(score);

            broadcastScoreUpdatedEvent(score);
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing message: " + e.getMessage());
        }
    }

    private void broadcastScoreUpdatedEvent(Score score) {
        try {
            ScoreUpdatedEvent scoreUpdatedEvent = new ScoreUpdatedEvent(
                    score.getUserId(),
                    score.getCreditScore()
            );
            String message = objectMapper.writeValueAsString(scoreUpdatedEvent);
            rabbitTemplate.convertAndSend(RabbitMQConstants.SCORE_UPDATED_QUEUE, message);
            System.out.println("Broadcast score updated event: " + message);
        } catch (JsonProcessingException e) {
            System.out.println("Error broadcasting score updated event: " + e.getMessage());
        }
    }
}
