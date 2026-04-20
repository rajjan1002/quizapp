package com.telusko.quizapp.model;

import lombok.Data;

@Data
public class QuestionDTO {
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String category;
    private String rightAnswer;
    private String difficultyLevel;
}