package com.quaser.edtechapp.rest.requests;

import com.quaser.edtechapp.Auth.AuthUtils;

import java.util.ArrayList;

public class SubmitTestReq {

    String user_id = AuthUtils.getUserId();
    String lesson_id;
    String unit_id;
    ArrayList<Answer> answers = new ArrayList<>();

    public SubmitTestReq(){}
    public SubmitTestReq(ArrayList<String> q, ArrayList<String> a, String lesson_id, String  unit_id){
        this.unit_id = unit_id;
        this.lesson_id = lesson_id;
        if (q != null){
            for (int i = 0; i < q.size(); i++){
                Answer answer = new Answer(q.get(i), a.get(i));
                answers.add(answer);
            }
        }
    }

    public class Answer{
        String question_id;
        String option_choosed;

        public Answer(){}
        public Answer(String question_id, String option_choosed){
            this.option_choosed = option_choosed;
            this.question_id = question_id;
        }
    }
}
