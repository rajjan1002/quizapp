package com.telusko.quizapp.controller;

import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.model.QuestionDTO;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AIQuestionService {

    @Autowired
    private OpenAiChatModel chatModel;

    public List<Question> generateQuestions(String category, int numQ) {

        // ✅ DTO converter (NOT entity)
        BeanOutputConverter<QuestionDTO[]> converter =
                new BeanOutputConverter<>(QuestionDTO[].class);

        String prompt = """
Generate %d multiple choice questions on topic %s.

Each question MUST include:
- questionTitle
- option1, option2, option3, option4
- rightAnswer
- category (must be "%s")
- difficultyLevel (easy, medium, or hard)

%s
""".formatted(
                numQ,
                category,
                category,
                converter.getFormat()
        );

        Prompt promptObj = new Prompt(prompt);

        String response = chatModel.call(promptObj)
                .getResult()
                .getOutput()
                .getText();

        // ✅ Convert to DTO
        QuestionDTO[] dtoArray = converter.convert(response);

        // ✅ Convert DTO → Entity
        List<Question> questions = new ArrayList<>();


        for (QuestionDTO dto : dtoArray) {
            Question q = new Question();

            q.setQuestionTitle(dto.getQuestionTitle());
            q.setOption1(dto.getOption1());
            q.setOption2(dto.getOption2());
            q.setOption3(dto.getOption3());
            q.setOption4(dto.getOption4());
            q.setRightAnswer(dto.getRightAnswer());
            q.setCategory(dto.getCategory());
            q.setDifficultyLevel(dto.getDifficultyLevel());

            // ❗ DO NOT SET ID → Hibernate will generate it
            questions.add(q);
        }

        return questions;
    }
}