package com.telusko.quizapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Response {
    private Integer id;
    private String response;

}
