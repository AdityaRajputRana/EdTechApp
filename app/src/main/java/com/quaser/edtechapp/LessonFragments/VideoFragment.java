package com.quaser.edtechapp.LessonFragments;

import android.app.AlertDialog;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.VideoLessonRP;
import com.quaser.edtechapp.utils.Method;

import java.util.ArrayList;


public class VideoFragment extends Fragment implements RevLessonInterface {

    LessonListener listener;
    ShortLesson shortLesson;
    VideoLessonRP videoLesson;
    String unitId;

    TextView titleTxt;
    TextView bodyTxt;
    ProgressBar progressBar;

    MaterialButton continueBtn;

    private PlayerView playerView;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private ExoPlayer player;

    private ArrayList<Pair<String, TrackSelectionOverrides.Builder>> qualityList
            = new ArrayList<Pair<String, TrackSelectionOverrides.Builder>>();
    private AlertDialog qualityPopup;
    private ImageButton exo_quality;
    private DefaultTrackSelector trackSelector;

    private AppCompatImageView fullScreenBtn;

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
        if (player != null)
        player.pause();
        super.onPause();
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    public VideoFragment(LessonListener listener, ShortLesson shortLesson, String unitId){
        this.listener = listener;
        this.shortLesson = shortLesson;
        this.unitId = unitId;
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
        trackSelector = new DefaultTrackSelector(getActivity(), factory);
        player = new ExoPlayer.Builder(getActivity())
                .setTrackSelector(
                        trackSelector
                ).build();

        Log.i("VOD", "PT2");
        controls.setPlayer(player);

        exo_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("VOD", "Quality Click");
                if (qualityPopup != null) {
                    Log.i("VOD", "Not null");
                    qualityPopup.show();
                }
            }
        });
        Log.i("VOD", "PT3");



        playerView.setPlayer(player);
    }

    private void setupQualityList(HlsManifest manifest) {
//        singleManifest(manifest);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_quality, null, false);
        ListView listView= view.findViewById(R.id.listView);


        ArrayList<String> qualities = new ArrayList<String>();
        for (HlsMasterPlaylist.Variant variant: manifest.masterPlaylist.variants){
            qualities.add(String.valueOf(variant.format.height));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , qualities);
        Log.i("VOD", "num qualities = " + qualities.size());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                qualityPopup.dismiss();
                TrackSelectionParameters parameters =
                        trackSelector.getParameters()
                                .buildUpon()
                                .setMaxVideoBitrate(manifest.masterPlaylist.variants.get(i).format.bitrate)
                                .setForceHighestSupportedBitrate(true)
                                .build();
                trackSelector.setParameters(parameters);
            }
        });

        qualityPopup = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("Select quality")
                .setView(view)
                .create();
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

//        continueBtn.setText(R.string.full_screen);
//        continueBtn.setVisibility(View.VISIBLE);
//        continueBtn.setOnClickListener(view -> fullScreen());
    }

    boolean isFullScreen = false;

    private void fullScreen() {
        listener.fullScreen(this);
        titleTxt.setVisibility(View.GONE);
        bodyTxt.setVisibility(View.GONE);
        controls.setVisibility(View.GONE);
        isFullScreen = true;
    }


    private void setUpMediaSource() {
        MediaItem item = MediaItem.fromUri(videoLesson.getVideo());
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(item);
        player.setMediaSource(hlsMediaSource);
        player.prepare();
        playerView.setVisibility(View.VISIBLE);
        player.setPlayWhenReady(true);
        continueBtn.setVisibility(View.GONE);
        player.addListener(
                new Player.Listener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_BUFFERING){
                            progressBar.setVisibility(View.VISIBLE);
                        } else if (playbackState == Player.STATE_READY)
                            progressBar.setVisibility(View.GONE);
                        else if (playbackState == Player.STATE_IDLE)
                            Toast.makeText(getActivity(), "Some error occurred!", Toast.LENGTH_SHORT).show();
                        else if (playbackState == Player.STATE_ENDED) {
                            Toast.makeText(getActivity(), "Ended", Toast.LENGTH_SHORT).show();
                            showCompleted();
                        }
                    }

                    @Override
                    public void onTimelineChanged(
                            Timeline timeline, @Player.TimelineChangeReason int reason) {
                        Object manifest = player.getCurrentManifest();
                        if (manifest != null) {
                            HlsManifest hlsManifest = (HlsManifest) manifest;
                            setupQualityList(hlsManifest);
                        }
                    }
                });
    }

    private void showCompleted() {
        continueBtn.setText("Next Lesson");
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });

        APIMethods.completeLesson(shortLesson.getId(), unitId, new APIResponseListener() {
            @Override
            public void success(Object response) {
                Toast.makeText(getActivity(), "Video completed! You can proceed to next lesson!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code + "-"  + message);
            }
        });

        continueBtn.setVisibility(View.VISIBLE);

    }

    private void proceed() {
        //Todo; Make this
        releasePlayer();
        listener.nextLesson();
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

    PlayerControlView controls;

    private void findViews(View view) {
        titleTxt = view.findViewById(R.id.title_txt);
        bodyTxt = view.findViewById(R.id.bodyTxt);
        progressBar = view.findViewById(R.id.progressBar);
        continueBtn = view.findViewById(R.id.continueBtn);
        playerView = view.findViewById(R.id.exoPlayer);
        controls = view.findViewById(R.id.controls);
        fullScreenBtn = controls.findViewById(R.id.fullScreenBtn);
        exo_quality = controls.findViewById(R.id.qualityBtn);
        setOnClicks();
    }

    private void setOnClicks() {
        fullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFullScreen();
            }
        });
    }

    @Override
    public void reverseFullScreen() {
        titleTxt.setVisibility(View.VISIBLE);
        bodyTxt.setVisibility(View.VISIBLE);
        listener.revFullScreen();
        controls.setVisibility(View.VISIBLE);
        isFullScreen =false;
    }

    void toggleFullScreen(){
        if (isFullScreen)
            reverseFullScreen();
        else
            fullScreen();
    }
}