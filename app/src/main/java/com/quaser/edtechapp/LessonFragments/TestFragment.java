package com.quaser.edtechapp.LessonFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.PersonalityTest.AnswerModel;
import com.quaser.edtechapp.PersonalityTest.EndAnswerModel;
import com.quaser.edtechapp.PersonalityTest.EndPersonalityTestRP;
import com.quaser.edtechapp.PersonalityTest.EndTestModel;
import com.quaser.edtechapp.PersonalityTest.EndTestReq;
import com.quaser.edtechapp.PersonalityTest.PersonalityTestModel;
import com.quaser.edtechapp.PersonalityTest.QuestionModel;
import com.quaser.edtechapp.PersonalityTest.TestModel;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.TestLessonRP;
import com.quaser.edtechapp.rest.response.TestRP;
import com.quaser.edtechapp.utils.Method;
import com.quaser.edtechapp.utils.PieChartUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import ir.mahozad.android.PieChart;

public class TestFragment extends Fragment implements RevLessonInterface{

    TestLessonRP testLesson;
    TestRP testRP;

    String unitId;
    ShortLesson shortLesson;
    LessonListener listener;

    int screenState = 0;

    //BriefLayout
    LinearLayout briefLayout;
    TextView bTitleTxt;
    TextView bHeadTxt;
    TextView bBodyTxt;
    TextView bQuestionsTxt;
    TextView bTimeTxt;
    LinearLayout bQuestionsLayout;
    LinearLayout bTimeLayout;
    MaterialButton startBtn;
    ProgressBar bProgressBar;

    //TestLayout
    LinearLayout testLayout;
    ImageButton backBtn;
    TextView questionsTxt;
    TextView timeLeftTxt;
    TextView questionTitle;
    TextView questionBody;
    ImageView questionImage;
    TextView optionA;
    TextView optionB;
    TextView optionC;
    TextView optionD;
    TextView optionE;
    ProgressBar progressBar;
    MaterialButton continueBtn;

    int currentQuestion = 1;

    PersonalityInterface personalityListener;

    public interface PersonalityInterface{
        void endTest(EndPersonalityTestRP response);
    }
    public TestFragment(String unitId, ShortLesson shortLesson, LessonListener listener){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }

    public TestFragment(ShortLesson personalityLesson, PersonalityInterface personalityListener){
        if (personalityLesson.getType().equals("PersonalityTest")){
            this.shortLesson = personalityLesson;
            this.personalityListener = personalityListener;
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        findViews(view);
        setUpTitles();
        if (shortLesson.getType().equals("PersonalityTest"))
            showStartControls();
        else
            fetchTest();
        return view;
    }

    private void showStartControls() {
        startBtn.setVisibility(View.VISIBLE);
        bProgressBar.setVisibility(View.GONE);
        startBtn.setOnClickListener(view -> startPersonalityTest());
    }

    private void fetchTest() {
        APIMethods.getLesson(shortLesson.getId(), TestLessonRP.class, new APIResponseListener<TestLessonRP>() {

            @Override
            public void success(TestLessonRP response) {
                bProgressBar.setVisibility(View.GONE);
                testLesson = response;
                showFullLesson();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                bProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showFullLesson() {
        bHeadTxt.setText(testLesson.getTitle());
        if (testLesson.getDescription() != null
                && !testLesson.getDescription().isEmpty()){
            bBodyTxt.setText(testLesson.getDescription());
        }
        bQuestionsTxt.setText(testLesson.getNum_questions()
                + " Questions");
        bTimeTxt.setText(Method.getTime(testLesson.getTime_allowed()));

        bQuestionsLayout.setVisibility(View.VISIBLE);
        bTimeLayout.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.VISIBLE);

        startBtn.setOnClickListener(view -> startTest());
    }


    private void startTest() {
        briefLayout.setVisibility(View.GONE);
        screenState = 1;
        listener.fullScreen(this);
        testLayout.setVisibility(View.VISIBLE);
        APIMethods.startTest(shortLesson.getId(), unitId, new APIResponseListener<TestRP>() {
            @Override
            public void success(TestRP response) {
                progressBar.setVisibility(View.GONE);
                testRP = response;
                loadTest();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                progressBar.setVisibility(View.GONE);
                briefLayout.setVisibility(View.VISIBLE);
                testLayout.setVisibility(View.GONE);
            }
        });
    }

    private PersonalityTestModel pTestRP;

    private void startPersonalityTest(){
        briefLayout.setVisibility(View.GONE);
        screenState = 1;
        testLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        APIMethods.startPersonalityTest(new APIResponseListener<PersonalityTestModel>() {
            @Override
            public void success(PersonalityTestModel response) {
                progressBar.setVisibility(View.GONE);
                pTestRP = response;
                loadTest();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                progressBar.setVisibility(View.GONE);
                briefLayout.setVisibility(View.VISIBLE);
                testLayout.setVisibility(View.GONE);
            }
        });
    }

    private void loadTest() {
        backBtn.setOnClickListener(view -> reverseFullScreen());
        backBtn.setVisibility(View.VISIBLE);

        if (!shortLesson.getType().equals("PersonalityTest")) {
            setCurrentQuestion();
            initiateTimer();
            timer.start();
            timeLeftTxt.setVisibility(View.VISIBLE);
        }

        initiateOptions();
        showQuestion();
    }

    private void initiateOptions() {
        Drawable inactiveBg = getActivity().getResources().getDrawable(R.drawable.bg_round_fg);
        Drawable activeBg = getActivity().getResources().getDrawable(R.drawable.bg_round_fg_selected);

        optionA.setOnClickListener(view -> {
            inactivateOptions(inactiveBg);
            if (selectedOption.equals("a")) {
                selectedOption = "-1";
                continueBtn.setEnabled(false);
            } else {
                selectedOption = "a";
                optionA.setBackground(activeBg);
                continueBtn.setEnabled(true);
            }
        });

        optionB.setOnClickListener(view -> {
            inactivateOptions(inactiveBg);
            if (selectedOption.equals("b")) {
                selectedOption = "-1";
                continueBtn.setEnabled(false);
            } else {
                selectedOption = "b";
                view.setBackground(activeBg);
                continueBtn.setEnabled(true);
            }
        });

        optionC.setOnClickListener(view -> {
            inactivateOptions(inactiveBg);
            if (selectedOption.equals("c")) {
                selectedOption = "-1";
                continueBtn.setEnabled(false);
            } else {
                selectedOption = "c";
                view.setBackground(activeBg);
                continueBtn.setEnabled(true);
            }
        });

        optionD.setOnClickListener(view -> {
            inactivateOptions(inactiveBg);
            if (selectedOption.equals("d")) {
                selectedOption = "-1";
                continueBtn.setEnabled(false);
            } else {
                selectedOption = "d";
                view.setBackground(activeBg);
                continueBtn.setEnabled(true);
            }
        });

        optionE.setOnClickListener(view -> {
            inactivateOptions(inactiveBg);
            if (selectedOption.equals("e")) {
                selectedOption = "-1";
                continueBtn.setEnabled(false);
            } else {
                selectedOption = "e";
                view.setBackground(activeBg);
                continueBtn.setEnabled(true);
            }
        });
    }

    String selectedOption ="-1";

    private void showQuestion() {
        if (shortLesson.getType().equals("PersonalityTest")){
            showPersonalityQuestion();
            return;
        }
        setCurrentQuestion();
        TestRP.Question question = testRP.getQuestions().get(currentQuestion-1);

        if (question.getQuestion() != null
                && !question.getQuestion().isEmpty()){
            questionTitle.setText(question.getQuestion());
            questionTitle.setVisibility(View.VISIBLE);
        } else {
            questionTitle.setVisibility(View.GONE);
        }

        if (question.getBody() != null
                && !question.getBody().isEmpty()){
            questionBody.setText(question.getBody());
            questionBody.setVisibility(View.VISIBLE);
        } else {
            questionBody.setVisibility(View.GONE);
        }

        if (question.getImage_url() != null
                && !question.getImage_url().isEmpty()){
            Picasso.get()
                    .load(question.getImage_url())
                    .into(questionImage);
            questionImage.setVisibility(View.VISIBLE);
        } else {
            questionImage.setVisibility(View.GONE);
        }

        Drawable inactiveBg = getActivity().getResources().getDrawable(R.drawable.bg_round_fg);
        Drawable activeBg = getActivity().getResources().getDrawable(R.drawable.bg_round_fg_selected);

        inactivateOptions(inactiveBg);
        if (question.getOptions() != null){
            if (question.getOptions().containsKey("a")){
                optionA.setText("A. " + question.getOptions().get("a"));
                optionA.setVisibility(View.VISIBLE);
            } else {
                optionA.setVisibility(View.GONE);
            }

            if (question.getOptions().containsKey("b")){
                optionB.setText("B. " + question.getOptions().get("b"));
                optionB.setVisibility(View.VISIBLE);
            } else {
                optionB.setVisibility(View.GONE);
            }

            if (question.getOptions().containsKey("c")){
                optionC.setText("C. " + question.getOptions().get("c"));
                optionC.setVisibility(View.VISIBLE);
            } else {
                optionC.setVisibility(View.GONE);
            }

            if (question.getOptions().containsKey("d")){
                optionD.setText("D. " + question.getOptions().get("d"));
                optionD.setVisibility(View.VISIBLE);
            } else {
                optionD.setVisibility(View.GONE);
            }
        } else {
            optionA.setVisibility(View.GONE);
            optionB.setVisibility(View.GONE);
            optionC.setVisibility(View.GONE);
            optionD.setVisibility(View.GONE);
        }

        if (currentQuestion == testRP.getQuestions().size()){
            continueBtn.setText("Submit Test");
            continueBtn.setOnClickListener(view -> {
                saveOption();
                submitTest();
            });
        } else {
            continueBtn.setText("Next Question");
            continueBtn.setOnClickListener(view -> {
                saveOption();
                showQuestion();
            });
        }

        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setEnabled(false);


    }

    private void showPersonalityQuestion() {
        setCurrentQuestion();
        int totalPartInTest = 0;
        for (int i = 0; i < currentPersonalityTest; i++)
            totalPartInTest = totalPartInTest+pTestRP.getTests().get(i).getQuestions().size();
        int actualQuestionIndex = currentQuestion - totalPartInTest -1;
        QuestionModel question = pTestRP.getTests().get(currentPersonalityTest).getQuestions().get(actualQuestionIndex);

        if (question.getQuestion() != null
                && !question.getQuestion().isEmpty()){
            questionTitle.setText(question.getQuestion());
            questionTitle.setVisibility(View.VISIBLE);
        } else {
            questionTitle.setVisibility(View.GONE);
        }

            questionBody.setVisibility(View.GONE);


        if (question.getImage() != null
                && !question.getImage().isEmpty()){
            Picasso.get()
                    .load(question.getImage())
                    .into(questionImage);
            questionImage.setVisibility(View.VISIBLE);
        } else {
            questionImage.setVisibility(View.GONE);
        }

        Drawable inactiveBg = getActivity().getResources().getDrawable(R.drawable.bg_round_fg);
        inactivateOptions(inactiveBg);
        ArrayList<TextView> optionTxts = new ArrayList<TextView>();
        optionTxts.add(optionA);
        optionTxts.add(optionB);
        optionTxts.add(optionC);
        optionTxts.add(optionD);
        optionTxts.add(optionE);
        for (TextView textView: optionTxts)
            textView.setVisibility(View.GONE);
        if (question.getOptions() != null){
            for (int i = 0; i <question.getOptions().size(); i++) {
                if (i > 4){
                    break;
                }
                TextView optionTxt = optionTxts.get(i);
                AnswerModel option = question.getOptions().get(i);
                optionTxt.setVisibility(View.VISIBLE);
                optionTxt.setText(option.getBody());
            }
        }

        int totalQuestions = 0;
        for (TestModel test: pTestRP.getTests())
            totalQuestions = totalQuestions+test.getQuestions().size();
        if (currentQuestion == totalQuestions){
            continueBtn.setText("Submit Test");
            continueBtn.setOnClickListener(view -> {
                savePersonalityOption();
                submitTest();
            });
        } else {
            continueBtn.setText("Next Question");
            continueBtn.setOnClickListener(view -> {
                savePersonalityOption();
                showQuestion();
            });
        }

        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setEnabled(false);
    }

    EndTestReq endTestReq;

    private void savePersonalityOption() {
        if (endTestReq == null){
            endTestReq = new EndTestReq();
        }

        int totalPartInTest = 0;
        for (int i = 0; i < currentPersonalityTest; i++)
            totalPartInTest = totalPartInTest+pTestRP.getTests().get(i).getQuestions().size();
        int actualQuestionIndex = currentQuestion - totalPartInTest -1;

        while (endTestReq.getTests().size() < currentPersonalityTest+1){
            endTestReq.getTests().add(new EndTestModel(
                    endTestReq.getTests().size(), pTestRP.getTests().get(endTestReq.getTests().size()).getId()
            ));
        }
        EndTestModel endTestModel = endTestReq.getTests().get(currentPersonalityTest);
        EndAnswerModel endAnswerModel = new EndAnswerModel();
        int answerIndex = -1;
        switch (selectedOption){
            case "a":
                answerIndex = 0;
                break;
            case "b":
                answerIndex = 1;
                break;
            case "c":
                answerIndex = 2;
                break;
            case "d":
                answerIndex = 3;
                break;
            case "e":
                answerIndex = 4;
                break;
        }

        endAnswerModel.setOption_index(answerIndex);
        endAnswerModel.setOption_id(pTestRP
                .getTests().get(currentPersonalityTest).getQuestions()
                .get(actualQuestionIndex).getOptions()
                .get(answerIndex).getId());

        endAnswerModel.setQuestion_index(actualQuestionIndex);
        endAnswerModel.setQuestion_id(pTestRP
                .getTests().get(currentPersonalityTest).getQuestions()
                .get(actualQuestionIndex).getId());

        endTestModel.getAnswers().add(endAnswerModel);
        if (actualQuestionIndex >= pTestRP.getTests()
                .get(currentPersonalityTest).getQuestions().size() - 1)
            currentPersonalityTest++;
        currentQuestion++;
    }

    private TestRP resultResponse;

    private LinearLayout resultLayout;
    private TextView resultInfoTxt;
    private TextView resultHeaderTxt;
    private PieChart pieChart;
    private LinearLayout analyticsLayout;
    private TextView correctAnswersTxt;
    private TextView wrongAnswersTxt;
    private MaterialButton nextLessonBtn;
    private ProgressBar resultProgressBar;

    private void findResultViews(View view){
        resultLayout = view.findViewById(R.id.resultLayout);
        resultInfoTxt = view.findViewById(R.id.resultInfoTxt);
        resultHeaderTxt = view.findViewById(R.id.resultHeaderTxt);
        pieChart = view.findViewById(R.id.pieChart);
        analyticsLayout = view.findViewById(R.id.analyticsLayout);
        correctAnswersTxt = view.findViewById(R.id.correctQuestion);
        wrongAnswersTxt = view.findViewById(R.id.wrongQuestions);
        nextLessonBtn = view.findViewById(R.id.nextLessonBtn);
        resultProgressBar = view.findViewById(R.id.resultProgress);
    }

    private void submitTest() {
        briefLayout.setVisibility(View.GONE);
        testLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
        resultProgressBar.setVisibility(View.VISIBLE);
        resultInfoTxt.setVisibility(View.VISIBLE);

        if (shortLesson.getType().equals("PersonalityTest")){
            APIMethods.endPersonalityTest(endTestReq, new APIResponseListener<EndPersonalityTestRP>() {
                @Override
                public void success(EndPersonalityTestRP response) {
                    personalityListener.endTest(response);
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    resultProgressBar.setVisibility(View.GONE);
                    testLayout.setVisibility(View.VISIBLE);
                    resultLayout.setVisibility(View.GONE);
                    Method.showFailedAlert(getActivity(), "Failed: "
                            + code + " - " + message);
                }
            });
        } else {

            APIMethods.submitTest(shortLesson.getId(), unitId, questionIds, answers, new APIResponseListener<TestRP>() {
                @Override
                public void success(TestRP response) {
                    resultInfoTxt.setVisibility(View.GONE);
                    resultProgressBar.setVisibility(View.GONE);
                    resultResponse = response;
                    showResult();
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    resultProgressBar.setVisibility(View.GONE);
                    testLayout.setVisibility(View.VISIBLE);
                    resultLayout.setVisibility(View.GONE);
                    Method.showFailedAlert(getActivity(), "Failed: "
                            + code + " - " + message);
                }
            });
        }
    }

    private void showResult() {
        listener.revFullScreen();
        String head = "You have scored ";
        int percent = resultResponse.getAwarded_marks()*100/resultResponse.getTotal_marks();
        head = percent+"% marks in this test.";

        resultHeaderTxt.setText(head);
        resultHeaderTxt.setVisibility(View.VISIBLE);

        new PieChartUtils().initialisePie(pieChart, percent, 100-percent,
                getResources().getColor(R.color.color_accent1_blue),
                getResources().getColor(R.color.color_accent3_red));
        pieChart.setVisibility(View.VISIBLE);

        correctAnswersTxt.setText(String.valueOf(resultResponse.getNum_correct()));
        wrongAnswersTxt.setText(String.valueOf(resultResponse.getNum_wrong()));

        analyticsLayout.setVisibility(View.VISIBLE);

        nextLessonBtn.setOnClickListener(view -> nextLesson());
        nextLessonBtn.setVisibility(View.VISIBLE);
    }

    private void nextLesson() {
        listener.revFullScreen();
        listener.nextLesson();
    }

    private void saveOption() {
        answers.add(selectedOption);
        questionIds.add(testRP.getQuestions().get(currentQuestion-1).get_id());
        currentQuestion++;
    }

    ArrayList<String> questionIds = new ArrayList<String>();
    ArrayList<String> answers = new ArrayList<>();

    private void inactivateOptions(Drawable inactiveBg) {
        optionA.setBackground(inactiveBg);
        optionB.setBackground(inactiveBg);
        optionC.setBackground(inactiveBg);
        optionD.setBackground(inactiveBg);
        optionE.setBackground(inactiveBg);
    }

    CountDownTimer timer;

    private void initiateTimer() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(testRP.getTime_allowed()* 1000L, 1000) {
            @Override
            public void onTick(long l) {
                //Todo: convert
                timeLeftTxt.setText(Method.getCountDownTime(l));
            }

            @Override
            public void onFinish() {
                finishTest();
            }
        };
    }

    private void finishTest() {
        submitTest();
    }

    int currentPersonalityTest = 0;

    private void setCurrentQuestion() {
        int totalQues = 0;
        if (shortLesson.getType().equals("PersonalityTest")){
            for (TestModel test: pTestRP.getTests()){
                totalQues = totalQues+test.getQuestions().size();
            }
        } else if (testRP != null
                && testRP.getQuestions() != null)
            totalQues = testRP.getQuestions().size();
        else
            totalQues = testLesson.getNum_questions();
        String txt = currentQuestion + " of "  + totalQues;
        questionsTxt.setVisibility(View.VISIBLE);
        questionsTxt.setText(txt);
    }


    private void setUpTitles() {
        if (shortLesson.getName() != null
                && !shortLesson.getName().isEmpty()) {
            bHeadTxt.setText(shortLesson.getName());
            bHeadTxt.setVisibility(View.VISIBLE);
            bHeadTxt.setText(shortLesson.getName());
        }

        if (shortLesson.getShort_description() != null
                && !shortLesson.getShort_description().isEmpty()) {
            bBodyTxt.setText(shortLesson.getShort_description());
            bBodyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View view) {
        briefLayout = view.findViewById(R.id.briefLayout);
        bTitleTxt = view.findViewById(R.id.testHead);
        bHeadTxt = view.findViewById(R.id.head);
        bBodyTxt = view.findViewById(R.id.descriptionTxt);
        bQuestionsLayout = view.findViewById(R.id.questionLL);
        bTimeLayout = view.findViewById(R.id.timeLL);
        bTimeTxt = view.findViewById(R.id.numTimeTxt);
        bQuestionsTxt = view.findViewById(R.id.numQuestionTxt);
        bProgressBar = view.findViewById(R.id.briefProgress);
        startBtn = view.findViewById(R.id.startTestBtn);

        testLayout = view.findViewById(R.id.testLayout);
        backBtn = view.findViewById(R.id.cancelTestBtn);
        questionsTxt = view.findViewById(R.id.questionsTxt);
        timeLeftTxt = view.findViewById(R.id.timeLeftTxt);
        questionTitle = view.findViewById(R.id.questionTitle);
        questionBody = view.findViewById(R.id.questionBody);
        questionImage = view.findViewById(R.id.imageView);
        optionA = view.findViewById(R.id.optionA);
        optionB = view.findViewById(R.id.optionB);
        optionC = view.findViewById(R.id.optionC);
        optionD = view.findViewById(R.id.optionD);
        optionE = view.findViewById(R.id.optionE);
        progressBar = view.findViewById(R.id.progressBar);
        continueBtn = view.findViewById(R.id.continueBtn);

        findResultViews(view);
    }

    @Override
    public void reverseFullScreen() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Exit test?")
                .setMessage("Your test progress will be lost if you exit the test")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitTest();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.fullScreen(TestFragment.this::reverseFullScreen);
                    }
                })
                .show();
    }

    private void exitTest() {
        timer.cancel();
        timer = null;

        briefLayout.setVisibility(View.VISIBLE);
        testLayout.setVisibility(View.GONE);
        //Todo Make This
    }
}