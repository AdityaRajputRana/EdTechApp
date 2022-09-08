package com.quaser.edtechapp.rest.response;

import java.util.ArrayList;
import java.util.HashMap;

public class TestRP {
    String _id;
    String title;
    int time_allowed;
    ArrayList<Question> questions;

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public int getTime_allowed() {
        return time_allowed;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public TestRP() {
    }

    public class Question{
        HashMap<String, String> options;
        String question;
        String image_url;
        String correct_option; //Todo: don't provide this.
        String _id;
        String body;

        public String getBody() {
            return body;
        }

        public HashMap<String, String> getOptions() {
            return options;
        }

        public String getQuestion() {
            return question;
        }

        public String getImage_url() {
            return image_url;
        }

        public String getCorrect_option() {
            return correct_option;
        }

        public String get_id() {
            return _id;
        }

        public Question() {
        }
    }
}
