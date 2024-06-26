package com.quaser.edtechapp.rest.api;

public class EndPoints {

    public static String anonymous = "auth/anonymous";
    public static String login = "auth/login";
    public static String updateName = "user/update/name";
    public static String isDeviceChanged = "auth/isDeviceChanged";
    public static String changeDevice = "auth/changeDevice";

    public static String home = "ui/home";
    public static String unit = "ui/unit";
    public static String lesson = "ui/lesson";
    public static String completeLesson = "ui/lesson/completed";
    public static String startTest = "ui/test/start";
    public static String submitTest = "ui/test/submit";
    public static String submitAssignment = "assignment/upload";

    public static String addQuestion = "forum/question/create";
    public static String getQuestion = "forum/question";
    public static String searchQuestion = "forum/getAllQuestion";
    public static String likeQuestion = "forum/question/like";
    public static String postAnswer = "forum/answer/create";

    public static String getLessonOrderId = "ui/lesson/payment";
    public static String verifyLessonPayment = "payment/paymentverification";

    public static String subscribeEvent = "event/subscribe";

    public static String updateDP = "user/displayPicture/update";
    public static String uploadFile = "ui/file-upload";

    public static String tags = "ui/tags";

    //Leaderboard
    public static String rankList = "ui/leaderboard/ranklist";

    public static String notification = "ui/notification/list";
    public static String assignments = "assignment/user/list";
    public static String paymentList = "payment/history";

    public static String personalityResult = "ui/personality/get";
    public static String startPersonalityTest = "ui/personality/startTest";
    public static String endPersonalityTest = "ui/personality/endTest";
    public static String subbedEvents = "ui/user/events";
    public static String eventDetails = "events/getDetails";
}
