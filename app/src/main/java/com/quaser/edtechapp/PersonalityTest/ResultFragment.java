package com.quaser.edtechapp.PersonalityTest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.R;
import com.squareup.picasso.Picasso;


public class ResultFragment extends Fragment {
    EndPersonalityTestRP personality;

    ImageView displayImg;
    TextView titleTxt;
    TextView headTxt;
    TextView bodyTxt;

    LinearLayout scoreLayout;

    TextView strengthsTxt;
    TextView weaknessTxt;
    TextView careerTxt;

    MaterialButton reattemptBtn;


    public ResultFragment() {
        // Required empty public constructor
    }
 
    
    public ResultFragment(EndPersonalityTestRP personality, ResultInterface listener){
        this.personality = personality;
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_result, container, false);
        findViews(view);
        showResult();
        return view;
    }

    private void showResult() {
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null
        && !FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString().isEmpty())
            Picasso.get()
                    .load(FirebaseAuth.getInstance()
                            .getCurrentUser().getPhotoUrl().toString())
                    .into(displayImg);

        titleTxt.setText(personality.title);
        headTxt.setText(personality.head);
        bodyTxt.setText(personality.body);

        String strengths = "";
        for (String s: personality.strengths){
            strengths = strengths + ", " + s;
        }
        strengths = strengths.substring(2);
        strengthsTxt.setText(strengths);

        String weakness = "";
        for (String s: personality.weakness){
            weakness = weakness + ", " + s;
        }
        weakness = weakness.substring(2);
        weaknessTxt.setText(weakness);

        String career = "";
        for (String s: personality.career_options){
            career = career+ ", " + s;
        }
        career = career.substring(2);
        careerTxt.setText(career);

        LayoutInflater inflater = LayoutInflater.from(scoreLayout.getContext());
        for (ScoreModel scoreModel: personality.scores){
            View view = inflater.inflate(R.layout.item_personality_score, scoreLayout, false);
            TextView pTitleTxt = view.findViewById(R.id.pTitleTxt);
            TextView nTitleTxt = view.findViewById(R.id.nTitleTxt);
            TextView pScoreTxt = view.findViewById(R.id.pScoreTxt);
            TextView nScoreTxt = view.findViewById(R.id.nScoreTxt);
            LinearProgressIndicator progress = view.findViewById(R.id.progress);

            pTitleTxt.setText(scoreModel.positive_title);
            nTitleTxt.setText(scoreModel.negative_title);
            pScoreTxt.setText(scoreModel.positive_score);
            nScoreTxt.setText(scoreModel.negative_score);

            int cta = getResources().getColor(R.color.color_cta);
            int pbg = getResources().getColor(R.color.progress_bg);

            if (scoreModel.getPositiveScore() < 50){
                cta = getResources().getColor(R.color.progress_bg);
                pbg = getResources().getColor(R.color.color_cta);
            }

            pTitleTxt.setTextColor(cta);
            pScoreTxt.setTextColor(cta);
            nScoreTxt.setTextColor(pbg);
            nTitleTxt.setTextColor(pbg);
            progress.setIndicatorColor(cta);
            progress.setTrackColor(pbg);

            progress.setProgress(scoreModel.getPositiveScore());
            scoreLayout.addView(view);
        }

        reattemptBtn.setOnClickListener(view -> listener.reattemptPersonalityTest());


    }
    ResultInterface listener;
    public interface ResultInterface{
        void reattemptPersonalityTest();
    }

    private void findViews(View view) {
        displayImg = view.findViewById(R.id.displayImg);
        titleTxt = view.findViewById(R.id.titleTxt);
        headTxt = view.findViewById(R.id.headTxt);
        bodyTxt = view.findViewById(R.id.bodyTxt);

        scoreLayout = view.findViewById(R.id.scoresLayout);

        strengthsTxt = view.findViewById(R.id.strengthsTxt);
        weaknessTxt = view.findViewById(R.id.weeknessTxt);
        careerTxt = view.findViewById(R.id.careerTxt);

        reattemptBtn = view.findViewById(R.id.reattemptBtn);
    }
}