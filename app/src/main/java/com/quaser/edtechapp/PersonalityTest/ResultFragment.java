package com.quaser.edtechapp.PersonalityTest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quaser.edtechapp.R;


public class ResultFragment extends Fragment {
    EndPersonalityTestRP personality;
    

    public ResultFragment() {
        // Required empty public constructor
    }
 
    
    public ResultFragment(EndPersonalityTestRP personality){
        this.personality = personality;
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
        return view;
    }

    private void findViews(View view) {
    }
}