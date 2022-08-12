package com.quaser.edtechapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quaser.edtechapp.LoginActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.HomeRP;
import com.quaser.edtechapp.utils.Method;

public class HomeFragment extends Fragment {

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
        fetch();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void fetch() {
        APIMethods.getHomeData(new APIResponseListener<HomeRP>() {
            @Override
            public void success(HomeRP response) {
                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code+ "-" + message);
            }
        }, getActivity());
    }
}