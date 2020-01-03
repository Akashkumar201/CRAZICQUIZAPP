package com.example.trivialapp.data;

import com.example.trivialapp.modle.Question;

import java.util.ArrayList;

public interface AnswerListAnsyncResponse {
    void processFinished(ArrayList<Question>questionArrayList);


}
