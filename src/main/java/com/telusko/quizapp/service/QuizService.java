package com.telusko.quizapp.service;

import com.telusko.quizapp.controller.AIQuestionService;
import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.dao.QuizDao;
import com.telusko.quizapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    private AIQuestionService aiQuestionService;
    // QuizService.java
    public ResponseEntity<String> createQuiz(String category, int numQ, String tittle) {
       // List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        List<Question> questions = aiQuestionService.generateQuestions(category, numQ);

        questionDao.saveAll(questions);
        Quiz quiz = new Quiz();
        quiz.setTittle(tittle);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>(String.valueOf(quiz.getId()), HttpStatus.CREATED); // ← return the ID
    }

    public List<QuestionWrapper> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();  //when we are dealing with Optional var then we need to access it by get.get
        // now to questions we get from DB needs to be converted to questionWrapper in order to send to client/ user
        // so we had to manually loop and convert list of questions to list of questionWrapper
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for (Question ques : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(ques.getId(), ques.getQuestionTitle(), ques.getOption1(), ques.getOption2(), ques.getOption3(), ques.getOption4());
            questionsForUser.add(qw);
        }


        return questionsForUser;
    }

    public ResponseEntity<QuizResult> calculateResult(Integer id, List<Response> responses) {

        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();

        int right = 0;
        List<AnswerResult> resultList = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {

            Response response = responses.get(i);
            Question question = questions.get(i);

            boolean isCorrect = response.getResponse().equals(question.getRightAnswer());

            if (isCorrect) right++;

            resultList.add(new AnswerResult(
                    question.getId(),
                    response.getResponse(),
                    question.getRightAnswer(),
                    isCorrect
            ));
        }

        QuizResult quizResult = new QuizResult(right, resultList);

        return new ResponseEntity<>(quizResult, HttpStatus.OK);
    }

}




