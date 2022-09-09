package com.quaser.edtechapp.rest.api;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.requests.AddQuestionRq;
import com.quaser.edtechapp.rest.requests.App.SubmitTestReq;
import com.quaser.edtechapp.rest.requests.CompleteLessonReq;
import com.quaser.edtechapp.rest.requests.HomeReq;
import com.quaser.edtechapp.rest.requests.LessonReq;
import com.quaser.edtechapp.rest.requests.LoginRequest;
import com.quaser.edtechapp.rest.requests.QuestionReq;
import com.quaser.edtechapp.rest.requests.UnitReq;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.rest.response.LoginRP;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.rest.response.HomeRP;
import com.quaser.edtechapp.rest.response.TestRP;
import com.quaser.edtechapp.rest.response.UnitRP;

import org.json.JSONObject;

import java.util.ArrayList;

public class APIMethods {
    public static void signInAnonymously(APIResponseListener<AnonymousRP> listener){
        LoginRequest req = new LoginRequest(true);
        API.postData(listener, req, EndPoints.anonymous, AnonymousRP.class);
    }

    public static void login(APIResponseListener<LoginRP> listener){
        LoginRequest req = new LoginRequest();
        API.postData(listener, req, EndPoints.login, LoginRP.class);
    }

    public static void changeName(String name, APIResponseListener<JSONObject> listener){
        LoginRequest req = new LoginRequest(name);
        API.postData(listener, req, EndPoints.updateName, JSONObject.class);
    }

    public static void getHomeData(APIResponseListener<HomeRP> listener, Activity context){
        HomeReq req = new HomeReq(context);
        API.postData(listener, req,  EndPoints.home, HomeRP.class);
    }

    public static void getUnit(String unitId, Activity context, APIResponseListener<UnitRP> listener){
        UnitReq req = new UnitReq(context, unitId);
        API.postData(listener, req, EndPoints.unit, UnitRP.class);
    }

    public static void postQuestion(String head, String body, String imageUrl, ArrayList<String> tags,
                                    Activity activity, APIResponseListener<QuestionRP> listener){
        AddQuestionRq req = new AddQuestionRq(imageUrl, tags, head, body, activity);
        API.postData(listener, req, EndPoints.addQuestion, QuestionRP.class);
    }

    public static void getForumHome(Activity context, APIResponseListener<ForumHomeRP> listener){
        HomeReq req = new HomeReq(context);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    public static void getQuestion(Activity context, String questionId, APIResponseListener<QuestionRP> listener){
        QuestionReq req = new QuestionReq(context, questionId);
        API.postData(listener, req, EndPoints.getQuestion, QuestionRP.class);
    }

    public static <K> void getLesson(String lessonId, Class responseType, APIResponseListener<K> listener){
        LessonReq req = new LessonReq(lessonId);
        API.postData(listener, req, EndPoints.lesson, responseType);
    }

    public static void completeLesson(String lessonId, String unitId, APIResponseListener listener){
        CompleteLessonReq req = new CompleteLessonReq(lessonId, unitId);
        API.postData(listener, req, EndPoints.completeLesson, Object.class);
    }

    public static void startTest(String lessonId, String unitId, APIResponseListener<TestRP> listener){
        CompleteLessonReq req = new CompleteLessonReq(lessonId, unitId);
        API.postData(listener, req, EndPoints.startTest, TestRP.class);
    }

    public static void submitTest(String lessonId, String unitId,
                                  ArrayList<String> questions, ArrayList<String> answers,
                                  APIResponseListener<TestRP> listener){
        SubmitTestReq req = new SubmitTestReq(questions, answers, lessonId, unitId);
        API.postData(listener, req, EndPoints.submitTest, TestRP.class);
    }


}
