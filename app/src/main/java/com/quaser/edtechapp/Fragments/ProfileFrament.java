package com.quaser.edtechapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.AssignmentsActivity;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.EventsActivity;
import com.quaser.edtechapp.LeaderboardActivity;
import com.quaser.edtechapp.LoginActivity;
import com.quaser.edtechapp.NameActivity;
import com.quaser.edtechapp.PaymentsActivity;
import com.quaser.edtechapp.PersonalityTest.PersonalityActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.squareup.picasso.Picasso;

public class ProfileFrament extends Fragment {


    public ProfileFrament() {
        // Required empty public constructor
    }


    private static ProfileFrament profileFrament;

    private TextView nameTxt;
    private TextView phoneTxt;
    private TextView signUpTxt;
    private LinearLayout userInfoLayout;
    private TextView editProfileTxt;
    private ImageView profileImg;

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
        View view = inflater.inflate(R.layout.fragment_profile_frament, container, false);
        findViews(view);
        setUpViews();
        setListeners();
        return view;
    }

    private void setListeners() {

    }

    private void setUpViews() {
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            editProfileTxt.setVisibility(View.GONE);
            phoneTxt.setVisibility(View.GONE);
            userInfoLayout.setOnClickListener(view -> login());
        } else {
            signUpTxt.setVisibility(View.GONE);
            if (AuthUtils.dp != null && !AuthUtils.dp.toString().isEmpty())
                Picasso.get()
                    .load(AuthUtils.dp)
                    .transform(new CircleTransform())
                    .into(profileImg);
            nameTxt.setText(AuthUtils.getUserName());
            phoneTxt.setText(AuthUtils.getFormattedPhoneNum());

            userInfoLayout.setOnClickListener(view -> {
                Intent i = new Intent(getActivity(), NameActivity.class);
                i.putExtra("EditProfileFlag", true);
                startActivity(i);
            });
        }
    }

    private void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("convertToPermanent", true);
        startActivity(intent);
    }

    private void findViews(View view) {
        MaterialButton logoutBtn = view.findViewById(R.id.logOutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogout();
            }
        });

        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        nameTxt = view.findViewById(R.id.nameTxt);
        phoneTxt = view.findViewById(R.id.phoneNumTxt);
        signUpTxt = view.findViewById(R.id.signUpTxt);
        editProfileTxt = view.findViewById(R.id.editProfileTxt);
        profileImg = view.findViewById(R.id.displayImg);

        view.findViewById(R.id.leaderboardBtn).setOnClickListener(mView -> {
            Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.assignmentsBtn).setOnClickListener(mView -> {
            Intent intent = new Intent(getActivity(), AssignmentsActivity.class);
            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.purchaseHistoryBtn).setOnClickListener(mView -> {
            Intent intent = new Intent(getActivity(), PaymentsActivity.class);
            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.personalityTestBtn).setOnClickListener(mView -> {
            Intent intent = new Intent(getActivity(), PersonalityActivity.class);
            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.subbedEventsBtn).setOnClickListener(mView -> {
            Intent intent = new Intent(getActivity(), EventsActivity.class);
            getActivity().startActivity(intent);
        });
    }

    private void confirmLogout() {
        String message = "Are you sure you want to logout?";
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            message = message + "\nWARNING: You are logged in as anonymous user. You will loose all your progress once you logout.";
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Log out")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        logout();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}