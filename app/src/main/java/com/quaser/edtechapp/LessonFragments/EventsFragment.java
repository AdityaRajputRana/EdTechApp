package com.quaser.edtechapp.LessonFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.Adapter.EventsRVAdapter;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortEvent;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.quaser.edtechapp.rest.response.EventRP;
import com.quaser.edtechapp.utils.Method;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private String unitId;
    private ShortLesson shortLesson;
    private LessonListener listener;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MaterialButton continueBtn;

    private EventsRVAdapter adapter;
    private EventRP eventRP;

    public EventsFragment() {
        // Required empty public constructor
    }

    public EventsFragment(String unitId, ShortLesson shortLesson, LessonListener listener) {
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        findViews(view);
        setVisibilities();
        setUpTitles();
        fetchEvents();
        return view;
    }

    private void fetchEvents() {
        APIMethods.getLesson(shortLesson.getId(),
                EventRP.class, new APIResponseListener<EventRP>() {
                    @Override
                    public void success(EventRP response) {
                        progressBar.setVisibility(View.GONE);
                        eventRP = response;
                        showFullLesson();
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        Method.showFailedAlert(getActivity(), "Failed: "
                                + code+" - " + message);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void showFullLesson() {
        adapter.addEvents(eventRP);
        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setOnClickListener(view -> completeLesson());
    }

    private void completeLesson() {
        progressBar.setVisibility(View.VISIBLE);
        continueBtn.setEnabled(false);
        APIMethods.completeLesson(shortLesson.getId(), unitId, new APIResponseListener() {
            @Override
            public void success(Object response) {
                listener.revFullScreen();
                listener.nextLesson();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code + "-"  + message);
                progressBar.setVisibility(View.GONE);
                continueBtn.setEnabled(true);
            }
        });
    }

    private void setUpTitles() {
        ArrayList<ShortEvent> events = new ArrayList<ShortEvent>();
        events.add(null);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        adapter = new EventsRVAdapter(events, shortLesson, getActivity(), unitId);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setVisibilities() {
        continueBtn.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        continueBtn = view.findViewById(R.id.continueBtn);
    }
}