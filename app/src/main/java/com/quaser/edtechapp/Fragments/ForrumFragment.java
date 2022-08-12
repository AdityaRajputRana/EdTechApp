package com.quaser.edtechapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quaser.edtechapp.R;

public class ForrumFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_forrum, container, false);
    }
}