package com.telusko.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuizResult {
    private int score;
    private List<AnswerResult> results;
}