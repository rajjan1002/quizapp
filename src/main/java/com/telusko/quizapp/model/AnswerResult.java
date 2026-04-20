package com.telusko.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerResult {
    private Integer questionId;
    private String userAnswer;
    private String correctAnswer;
    private boolean correct;
}