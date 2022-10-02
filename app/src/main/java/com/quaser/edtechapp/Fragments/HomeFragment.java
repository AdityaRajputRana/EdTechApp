package com.quaser.edtechapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.HomeRVAdapter;
import com.quaser.edtechapp.LoginActivity;
import com.quaser.edtechapp.NotificationActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.HomeRP;
import com.quaser.edtechapp.utils.Method;

public class HomeFragment extends Fragment {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    HomeRVAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static HomeFragment homeFragment;

    public static HomeFragment newInstance(){
        if (homeFragment == null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        fetch();
        return view;
    }

    private void fetch() {
        APIMethods.getHomeData(new APIResponseListener<HomeRP>() {
            @Override
            public void success(HomeRP response) {
                progressBar.setVisibility(View.GONE);
                adapter = new HomeRVAdapter(response, getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code+ "-" + message);
                progressBar.setVisibility(View.GONE);
            }
        }, getActivity());
    }
}