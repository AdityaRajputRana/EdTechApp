package com.quaser.edtechapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quaser.edtechapp.Adapter.ForumHomeRVAdapter;
import com.quaser.edtechapp.AddQuestionActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.utils.Method;

public class ForrumFragment extends Fragment {
    ExtendedFloatingActionButton addBtn;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    EditText searchEt;
    ForumHomeRVAdapter adapter;

    public static ForrumFragment forrumFragment;

    public ForrumFragment() {
        // Required empty public constructor
    }

    public static ForrumFragment newInstance(){
        if (forrumFragment == null){
            forrumFragment = new ForrumFragment();
        }
        return forrumFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forrum, container, false);
        findViews(view);
        fetchForumQuestions();
        return view;
    }

    private void fetchForumQuestions() {
        //Todo: Add Pagination
        APIMethods.getForumHome(getActivity(), new APIResponseListener<ForumHomeRP>() {
            @Override
            public void success(ForumHomeRP response) {
                progressBar.setVisibility(View.GONE);
                showRecyclerView(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code+"-"+message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showRecyclerView(ForumHomeRP res) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        adapter = new ForumHomeRVAdapter(res, getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void findViews(View view) {
        addBtn = view.findViewById(R.id.addBtn);
        searchEt = view.findViewById(R.id.searchEt);
        recyclerView = view.findViewById(R.id.questionRV);
        progressBar = view.findViewById(R.id.progressBar);
        addListeners();
    }

    private void addListeners() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddActivity();
            }
        });
    }

    private void launchAddActivity() {
        Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
        startActivity(intent);
    }
}