package com.quaser.edtechapp.LessonFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;


public class AssignmentFragment extends Fragment {

    private String unitId;
    private ShortLesson shortLesson;
    private LessonListener listener;


    public AssignmentFragment() {
        // Required empty public constructor
    }

    public AssignmentFragment(String unitId, ShortLesson shortLesson, LessonListener listener){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignment, container, false);
    }
}