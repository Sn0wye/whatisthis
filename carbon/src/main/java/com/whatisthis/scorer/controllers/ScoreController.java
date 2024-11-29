package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.dto.CalculateScoreDTO;
import com.whatisthis.scorer.model.request.CalculateScoreRequest;
import com.whatisthis.scorer.model.request.UpdateScoreRequest;
import com.whatisthis.scorer.model.response.ScoreResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Scorer", description = "Operations related to credit score")
@RestController
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @Operation(description = "Get credit score")
    @GetMapping("/score")
    public ResponseEntity<ScoreResponse> getScore(HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();

        Score score = scoreRepository.findByUserId(userId).orElse(null);

        if (score == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getCreditScore()
                )
        );
    }

    @Operation(description = "Calculate credit score")
    @PostMapping("/score/calculate")
    public ResponseEntity<ScoreResponse> calculateScore(HttpServletRequest request, @Valid @RequestBody CalculateScoreRequest dto) {
        String userId = request.getAttribute("userId").toString();

        int creditScore = calculateCreditScoreService.execute(
                dto.incomeAsDecimal(),
                dto.debtAsDecimal(),
                dto.assetsValueAsDecimal()
        );

        Score score = scoreRepository.findByUserId(userId).orElseGet(Score::new);

        score.setUserId(userId);
        score.setIncome(dto.incomeAsDecimal());
        score.setDebt(dto.debtAsDecimal());
        score.setAssetsValue(dto.assetsValueAsDecimal());
        score.setCreditScore(creditScore);

        scoreRepository.save(score);

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getCreditScore()
                )
        );
    }

    @Operation(description = "Update credit score")
    @PostMapping("/score/update")
    public ResponseEntity<ScoreResponse> updateScore(HttpServletRequest request, @Valid @RequestBody UpdateScoreRequest body) {
        String userId = request.getAttribute("userId").toString();

        Score existingScore = scoreRepository.findByUserId(userId).orElse(null);

        if (existingScore == null) {
            return ResponseEntity.notFound().build();
        }

        existingScore.setIncome(body.incomeAsDecimal());
        existingScore.setDebt(body.debtAsDecimal());
        existingScore.setAssetsValue(body.assetsValueAsDecimal());

        int creditScore = calculateCreditScoreService.execute(
                existingScore.getIncome(),
                existingScore.getDebt(),
                existingScore.getAssetsValue()
        );

        existingScore.setCreditScore(creditScore);

        scoreRepository.save(existingScore);

        return ResponseEntity.ok(
                new ScoreResponse(
                        existingScore.getCreditScore()
                )
        );
    }
}
