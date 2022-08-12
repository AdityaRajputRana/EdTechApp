package com.quaser.edtechapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quaser.edtechapp.R;

public class ProfileFrament extends Fragment {


    public ProfileFrament() {
        // Required empty public constructor
    }


    private static ProfileFrament profileFrament;

    public static ProfileFrament newInstance(){
        if (profileFrament == null){
            profileFrament= new ProfileFrament();
        }
        return profileFrament;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_frament, container, false);
    }
}