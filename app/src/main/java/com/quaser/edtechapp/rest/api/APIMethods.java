package com.quaser.edtechapp.rest.api;

import android.app.Activity;

import com.quaser.edtechapp.PersonalityTest.EndPersonalityTestRP;
import com.quaser.edtechapp.PersonalityTest.EndTestReq;
import com.quaser.edtechapp.PersonalityTest.PersonalityTestModel;
import com.quaser.edtechapp.PersonalityTest.PersonalityTestRP;
import com.quaser.edtechapp.models.Answer;
import com.quaser.edtechapp.models.NotificationModel;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.requests.AddAnswerRQ;
import com.quaser.edtechapp.rest.requests.AddQuestionRq;
import com.quaser.edtechapp.rest.requests.ChangeDeviceReq;
import com.quaser.edtechapp.rest.requests.EventReq;
import com.quaser.edtechapp.rest.requests.ForumReq;
import com.quaser.edtechapp.rest.requests.SubmitAssignmentReq;
import com.quaser.edtechapp.rest.requests.SubmitTestReq;
import com.quaser.edtechapp.rest.requests.CompleteLessonReq;
import com.quaser.edtechapp.rest.requests.HomeReq;
import com.quaser.edtechapp.rest.requests.LessonReq;
import com.quaser.edtechapp.rest.requests.LoginRequest;
import com.quaser.edtechapp.rest.requests.ForumQAReq;
import com.quaser.edtechapp.rest.requests.SubscribeEventReq;
import com.quaser.edtechapp.rest.requests.UnitReq;
import com.quaser.edtechapp.rest.requests.UploadFileReq;
import com.quaser.edtechapp.rest.requests.VerifyLessonPaymentReq;
import com.quaser.edtechapp.rest.response.AssignmentListRP;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.quaser.edtechapp.rest.response.ChangeDeviceRP;
import com.quaser.edtechapp.rest.response.DataRp;
import com.quaser.edtechapp.rest.response.DeviceChangeRP;
import com.quaser.edtechapp.rest.response.EventDetailsRP;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.quaser.edtechapp.rest.response.LoginRP;
import com.quaser.edtechapp.rest.response.PaymentsListRP;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.rest.response.HomeRP;
import com.quaser.edtechapp.rest.response.RanklistRes;
import com.quaser.edtechapp.rest.response.SubbedEvents;
import com.quaser.edtechapp.rest.response.SubscribeEventRP;
import com.quaser.edtechapp.rest.response.TagsRP;
import com.quaser.edtechapp.rest.response.TestRP;
import com.quaser.edtechapp.rest.response.UnitRP;
import com.quaser.edtechapp.rest.response.VerifiedPaymentRP;
import com.quaser.edtechapp.utils.Tools;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

public class APIMethods {
    public static void signInAnonymously(APIResponseListener<AnonymousRP> listener){
        LoginRequest req = new LoginRequest(true);
        API.postData(listener, req, EndPoints.anonymous, AnonymousRP.class);
    }

    public static void getTags(APIResponseListener<TagsRP> listener){
        API.postData(listener, "{}", EndPoints.tags, TagsRP.class);
    }

    public static void login(Activity activity, APIResponseListener<LoginRP> listener){
        LoginRequest req = new LoginRequest(activity);
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

    public static void postQuestion(String head, String body, String html, String serialised, ArrayList<String> media, ArrayList<String> tags,
                                    Activity activity, APIResponseListener<QuestionRP> listener){
        String imageUrl = "";
        if (media != null && media.size() >0){
            imageUrl = media.get(0);
        }
        AddQuestionRq req = new AddQuestionRq(imageUrl, tags, head, body, activity, html, serialised, media);
        API.postData(listener, req, EndPoints.addQuestion, QuestionRP.class);
    }

    public static void postAnswer(AddAnswerRQ req, APIResponseListener<QuestionRP> listener){
        API.postData(listener, req, EndPoints.postAnswer, QuestionRP.class); //Todo change to AnswerRP class
    }

    public static void getForumQuestion(Activity context, APIResponseListener<ForumHomeRP> listener){
        ForumReq req = new ForumReq();
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    //Search QUESTION
    public static void getForumQuestion(String keyword, APIResponseListener<ForumHomeRP> listener){
        ForumReq req = new ForumReq(keyword);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    //PAGINATE SEARCH QUESTION
    public static void getForumQuestion(String keyword, int page, APIResponseListener<ForumHomeRP> listener){
        ForumReq req = new ForumReq(keyword, page);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    //Paginate Tagged question
    public static void getForumQuestion(int page, APIResponseListener<ForumHomeRP> listener, String tag){
        ForumReq req = new ForumReq(page, tag);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    //Get Tagged questions
    public static void getForumQuestion(APIResponseListener<ForumHomeRP> listener, String tag){
        ForumReq req = new ForumReq(true, tag);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    public static void getForumQuestion(int page, APIResponseListener<ForumHomeRP> listener){
        ForumReq req = new ForumReq(page);
        API.postData(listener, req, EndPoints.searchQuestion, ForumHomeRP.class);
    }

    public static void likeQuestion(String questionId, APIResponseListener<QuestionRP> listener){
        ForumQAReq req = new ForumQAReq(questionId);
        //Todo: change from string to success req
        API.postData(listener, req, EndPoints.likeQuestion, QuestionRP.class);
    }

    public static void upVoteAnswer(String questionId, String answerId, APIResponseListener<Answer> listener){
        ForumQAReq req = new ForumQAReq(questionId, answerId);
        //Todo: change from string to success req
        API.postData(listener, req, EndPoints.likeQuestion, Answer.class);
    }



    public static void getQuestion(Activity context, String questionId, APIResponseListener<QuestionRP> listener){
        ForumQAReq req = new ForumQAReq(context, questionId);
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

    public static void uploadPDFAssignment(String lessonId, String unitId,
                                        String pdfFile, APIResponseListener<AssignmentRP> listener){
        SubmitAssignmentReq req
                = new SubmitAssignmentReq(lessonId, unitId, pdfFile);
        API.postData(listener, req, EndPoints.submitAssignment, AssignmentRP.class);
    }

    public static void updateDP(String profilePicture, String ext, APIResponseListener<DataRp> listener){
        UploadFileReq req =
                new UploadFileReq(profilePicture, ext);
        API.postData(listener, req, EndPoints.updateDP, DataRp.class);
    }

    public static void uploadForumFile(String profilePicture, String ext, APIResponseListener<DataRp> listener){
        UploadFileReq req =
                new UploadFileReq(profilePicture, ext);
        API.postData(listener, req, EndPoints.uploadFile, DataRp.class);
    }



    public static void getOrderId(String lessonId, String unitId, APIResponseListener<LessonOrderIdRp> listener){
        LessonReq req = new LessonReq(lessonId, unitId);
        API.postData(listener, req, EndPoints.getLessonOrderId, LessonOrderIdRp.class);
    }

    public static void verifyLessonPayment(String s, String order_id, APIResponseListener<VerifiedPaymentRP> listener){
        VerifyLessonPaymentReq req = VerifyLessonPaymentReq.getInstance(s, order_id);
        API.postData(listener, req, EndPoints.verifyLessonPayment, VerifiedPaymentRP.class);
    }

    public static <K> void verifyLessonPayment(String s, String orderId, Class responseType, APIResponseListener<K> listener){
        VerifyLessonPaymentReq req = VerifyLessonPaymentReq.getInstance(s, orderId);
        API.postData(listener, req, EndPoints.verifyLessonPayment, responseType);
    }

    public static void subscribeToFreeEvent(String eventId, String unitId, String lessonId, APIResponseListener<SubscribeEventRP> listener){
        SubscribeEventReq req = new SubscribeEventReq(lessonId, unitId, eventId);
        API.postData(listener, req, EndPoints.subscribeEvent, SubscribeEventRP.class);
    }

    public static void subscribeToPaidEvent(String eventId, String unitId, String lessonId, APIResponseListener<LessonOrderIdRp> listener){
        SubscribeEventReq req = new SubscribeEventReq(lessonId, unitId, eventId);
        API.postData(listener, req, EndPoints.subscribeEvent, LessonOrderIdRp.class);
    }

    //Leaderboard
    public static void getRanklist(APIResponseListener<RanklistRes> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.rankList, RanklistRes.class);
    }

    //paginated leaderboard
    public static void getRanklist(int page, APIResponseListener<RanklistRes> listener){
        HomeReq req = new HomeReq(page);
        API.postData(listener, req, EndPoints.rankList, RanklistRes.class);
    }

    //Noti
    public static void getNotification(APIResponseListener<ArrayList<NotificationModel>> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.notification, ArrayList.class);
    }

    //Paginated notif
    public static void getNotification(int page, APIResponseListener<ArrayList<NotificationModel>> listener){
        HomeReq req = new HomeReq(page);
        API.postData(listener, req, EndPoints.notification, ArrayList.class);
    }

    //assignment
    public static void getAssignments(APIResponseListener<AssignmentListRP> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.assignments, AssignmentListRP.class);
    }

    //Events
    public static void getSubbedEvents(APIResponseListener<SubbedEvents> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.subbedEvents, SubbedEvents.class);
    }

    public static void getEventDetails(String eventId, APIResponseListener<EventDetailsRP> listener){
        EventReq req = new EventReq(eventId);
        API.postData(listener, req, EndPoints.eventDetails, EventDetailsRP.class);
    }



    //Paginated notif
    public static void getAssignments(int page, APIResponseListener<AssignmentListRP> listener){
        HomeReq req = new HomeReq(page);
        API.postData(listener, req, EndPoints.notification, AssignmentListRP.class);
    }

    public static void getPayments(APIResponseListener<PaymentsListRP> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.paymentList, PaymentsListRP.class);
    }


    public static void getPersonalityTest(APIResponseListener<PersonalityTestRP> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.personalityResult, PersonalityTestRP.class);
    }

    public static void startPersonalityTest(APIResponseListener<PersonalityTestModel> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.startPersonalityTest, PersonalityTestModel.class);
    }

    public static void endPersonalityTest(EndTestReq req, APIResponseListener<EndPersonalityTestRP> listener){
        API.postData(listener, req, EndPoints.endPersonalityTest, EndPersonalityTestRP.class);
    }

    public static void isDeviceChanged(APIResponseListener<DeviceChangeRP> listener){
        HomeReq req = new HomeReq();
        API.postData(listener, req, EndPoints.isDeviceChanged, DeviceChangeRP.class);
    }

    public static void changeDevice(Activity activity, String message, APIResponseListener<ChangeDeviceRP> listener){
        ChangeDeviceReq req = new ChangeDeviceReq(Tools.getUniqueDeviceId(activity), message);
        API.postData(listener, req, EndPoints.changeDevice, ChangeDeviceRP.class);
    }


}
