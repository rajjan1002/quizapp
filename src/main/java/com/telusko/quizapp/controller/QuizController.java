package com.telusko.quizapp.controller;

import com.telusko.quizapp.model.QuestionWrapper;
import com.telusko.quizapp.model.QuizResult;
import com.telusko.quizapp.model.Response;
import com.telusko.quizapp.service.QuizService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {
    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String tittle) {

        return quizService.createQuiz(category, numQ, tittle);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        // Call the service to get the questions
        List<QuestionWrapper> questionsForUser = quizService.getQuizQuestions(id);
        // Return the response with the list of QuestionWrapper DTOs and HTTP status 200 (OK)
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }
    @PostMapping("submit/{id}")
    public ResponseEntity<QuizResult> submitQuiz(
            @PathVariable Integer id,
            @RequestBody List<Response> responses) {

        return quizService.calculateResult(id, responses);
    }


}
