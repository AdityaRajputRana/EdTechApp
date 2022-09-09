package com.quaser.edtechapp.rest.response;

import java.util.ArrayList;
import java.util.HashMap;

public class TestRP {
    String _id;
    String title;
    int time_allowed;
    ArrayList<Question> questions;

    int awarded_marks;
    int total_marks;
    int num_correct;
    int num_wrong;

    public int getAwarded_marks() {
        return awarded_marks;
    }

    public int getTotal_marks() {
        return total_marks;
    }

    public int getNum_correct() {
        return num_correct;
    }

    public int getNum_wrong() {
        return num_wrong;
    }

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

        String chosen_option;
        int awarded_marks;
        int max_marks;

        public String getChosen_option() {
            return chosen_option;
        }

        public int getAwarded_marks() {
            return awarded_marks;
        }

        public int getMax_marks() {
            return max_marks;
        }

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
