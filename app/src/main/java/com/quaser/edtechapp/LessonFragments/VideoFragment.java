package com.quaser.edtechapp.LessonFragments;

import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.VideoLessonRP;
import com.quaser.edtechapp.utils.Method;


public class VideoFragment extends Fragment {

    LessonListener listener;
    ShortLesson shortLesson;
    VideoLessonRP videoLesson;

    TextView titleTxt;
    TextView bodyTxt;
    ProgressBar progressBar;

    MaterialButton continueBtn;

    private PlayerView playerView;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private ExoPlayer player;

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public void onPause() {
        releasePlayer();
        super.onPause();
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    public VideoFragment(LessonListener listener, ShortLesson shortLesson){
        this.listener = listener;
        this.shortLesson = shortLesson;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        findViews(view);
        setUpTitles();
        setUpPlayer();
        fetchVideoLesson();
        return view;
    }

    private void setUpPlayer() {

        AdaptiveTrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(
                4000, 1000, 2500,
                0.8f
        );
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(getActivity(), factory);
        player = new ExoPlayer.Builder(getActivity())
                .setTrackSelector(
                        trackSelector
                ).build();


        playerView.setPlayer(player);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                    setUpQualityOption();
                }
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == PlaybackState.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == PlaybackState.STATE_PLAYING)
                    progressBar.setVisibility(View.GONE);
                else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    setUpQualityOption();
                }


            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
//                if (isLoading){
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                }
//                Player.Listener.super.onIsLoadingChanged(isLoading);
            }
        });
    }

    private void setUpQualityOption() {
        Log.i("VOD", "Quality options");
    }

    private void fetchVideoLesson() {
        APIMethods.getLesson(shortLesson.getId(), VideoLessonRP.class, new APIResponseListener<VideoLessonRP>() {
            @Override
            public void success(VideoLessonRP response) {
                progressBar.setVisibility(View.GONE);
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

    private void showFullLesson(VideoLessonRP response) {
        videoLesson = response;
        shortLesson.setName(response.getTitle());
        shortLesson.setShort_description(response.getShort_description());
        setUpTitles();
        setUpMediaSource();

        continueBtn.setText(R.string.play_vid);
        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setOnClickListener(view -> playVideo());
    }

    private void setUpMediaSource() {
        Log.i("VOD", videoLesson.getVideo());


        MediaItem item = MediaItem.fromUri(videoLesson.getVideo());
        player.setMediaItem(item);
        player.prepare();
        playerView.setVisibility(View.VISIBLE);
    }

    public void playVideo(){
        player.play();
        continueBtn.setVisibility(View.GONE);
        continueBtn.setText("Next Lesson");
    }


    private void setUpTitles() {
        if (shortLesson.getName() != null
                && !shortLesson.getName().isEmpty())
            titleTxt.setText(shortLesson.getName());
        else
            titleTxt.setVisibility(View.GONE);

        if (shortLesson.getShort_description() != null
                && !shortLesson.getShort_description().isEmpty())
            bodyTxt.setText(shortLesson.getShort_description());
        else
            bodyTxt.setVisibility(View.GONE);
    }

    private void findViews(View view) {
        titleTxt = view.findViewById(R.id.title_txt);
        bodyTxt = view.findViewById(R.id.bodyTxt);
        progressBar = view.findViewById(R.id.progressBar);
        continueBtn = view.findViewById(R.id.continueBtn);
        playerView = view.findViewById(R.id.exoPlayer);
    }
}