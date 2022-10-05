package com.quaser.edtechapp.LessonFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.ArticleLessonRP;
import com.quaser.edtechapp.utils.Method;
import com.quaser.edtechapp.utils.WsyswigUtils;
import com.quaser.edtechapp.wsywig.Editor;


public class ArticleFragment extends Fragment implements RevLessonInterface {

    private TextView titleTxt;
    private TextView headTxt;
    private TextView descriptionTxt;
    private MaterialButton continueBtn;
    private ProgressBar progressBar;

    private String unitId;
    private ShortLesson shortLesson;
    private LessonListener listener;

    private ArticleLessonRP articleLesson;

    private LinearLayout shortLayout;
    private LinearLayout fullLayout;
    private TextView head;
    private TextView body;
    private Editor bodyEditor;
    private TextView title;
    private MaterialButton completeBtn;
    private ImageButton backBtn;




    public ArticleFragment() {
        // Required empty public constructor
    }

    public ArticleFragment(String unitId, ShortLesson shortLesson, LessonListener listener){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        findViews(view);
        setUpTitles();
        fetchArticle();
        return view;
    }

    private void fetchArticle() {
        APIMethods.getLesson(shortLesson.getId(), ArticleLessonRP.class, new APIResponseListener<ArticleLessonRP>() {

            @Override
            public void success(ArticleLessonRP response) {
                progressBar.setVisibility(View.GONE);
                descriptionTxt.setVisibility(View.VISIBLE);
                articleLesson = response;
                showFullLesson(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showFullLesson(ArticleLessonRP response) {

        headTxt.setText(response.getHead());
        head.setText(response.getHead());

        if (response.getBody().contains("<!DOCTYPE_HTML>")
                || response.getBody().contains("<!DOCTYPE html>")){
            String b = response.getBody().replace("<!DOCTYPE_HTML>", "");
            bodyEditor.setVisibility(View.VISIBLE);
            WsyswigUtils.renderBody(bodyEditor, b, getActivity());
        } else {
            body.setVisibility(View.VISIBLE);
            body.setText(response.getBody());
        }
        headTxt.setVisibility(View.VISIBLE);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.fullScreen(ArticleFragment.this);
                shortLayout.setVisibility(View.GONE);
                fullLayout.setVisibility(View.VISIBLE);
                //Todo change to full screen and show different layout.
            }
        });
        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setText(R.string.read);

        completeBtn.setOnClickListener(view -> nextLesson());
    }

    private void nextLesson() {
        completeBtn.setEnabled(false);
        backBtn.setEnabled(false);
        markCompleted();
    }

    private void markCompleted() {
        APIMethods.completeLesson(shortLesson.getId(), unitId, new APIResponseListener() {
            @Override
            public void success(Object response) {
                Toast.makeText(getActivity(), "Article Read! You can proceed to next lesson!", Toast.LENGTH_SHORT).show();
                listener.revFullScreen();
                listener.nextLesson();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code + "-"  + message);
            }
        });
    }

    private void setUpTitles() {
        if (shortLesson.getName() != null
                && !shortLesson.getName().isEmpty()) {
            titleTxt.setText(shortLesson.getName());
            titleTxt.setVisibility(View.VISIBLE);
            title.setText(shortLesson.getName());
        }

        if (shortLesson.getShort_description() != null
                && !shortLesson.getShort_description().isEmpty()) {
            descriptionTxt.setText(shortLesson.getShort_description());
            descriptionTxt.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(view -> showShortLayout());
    }

    private void showShortLayout() {
        fullLayout.setVisibility(View.GONE);
        shortLayout.setVisibility(View.VISIBLE);
        listener.revFullScreen();
    }

    private void findViews(View view){
        headTxt = view.findViewById(R.id.headTxt);
        titleTxt = view.findViewById(R.id.title_txt);
        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        continueBtn = view.findViewById(R.id.continueBtn);
        progressBar = view.findViewById(R.id.progressBar);

        head = view.findViewById(R.id.head);
        body = view.findViewById(R.id.body);
        completeBtn = view.findViewById(R.id.completeBtn);
        backBtn = view.findViewById(R.id.backBtn);
        title = view.findViewById(R.id.titleTxt);

        shortLayout = view.findViewById(R.id.shortLayout);
        fullLayout = view.findViewById(R.id.fullLayout);
        bodyEditor = view.findViewById(R.id.bodyEditor);
    }

    @Override
    public void reverseFullScreen() {
        showShortLayout();
    }
}