package com.quaser.edtechapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.quaser.edtechapp.Fragments.ForrumFragment;
import com.quaser.edtechapp.Fragments.HomeFragment;
import com.quaser.edtechapp.Fragments.ProfileFrament;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private ForrumFragment forrumFragment;
    private ProfileFrament profileFrament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFragsNBottomBar();
    }

    private void setUpFragsNBottomBar() {

        homeFragment  = HomeFragment.newInstance();
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragmentLayout, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        AnimatedBottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int mI, @NonNull AnimatedBottomBar.Tab tab1) {
               index = mI;
               changeFragment(mI);
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                //Todo: Add Reloading of fragment here
            }
        });
    }

    private void changeFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index){
            case 0:
                if(homeFragment == null){
                    homeFragment = HomeFragment.newInstance();
                    transaction.add(R.id.fragmentLayout, homeFragment);
                }
                transaction.show(homeFragment);
                if (profileFrament != null){
                    transaction.hide(profileFrament);
                }

                if (forrumFragment != null){
                    transaction.hide(forrumFragment);
                }
                break;

            case 1:
                if(forrumFragment == null){
                    forrumFragment = ForrumFragment.newInstance();
                    transaction.add(R.id.fragmentLayout, forrumFragment);
                }
                transaction.show(forrumFragment);
                if (profileFrament != null){
                    transaction.hide(profileFrament);
                }

                if (homeFragment != null){
                    transaction.hide(homeFragment);
                }
                break;

            case 2:
                if(profileFrament == null){
                    profileFrament = ProfileFrament.newInstance();
                    transaction.add(R.id.fragmentLayout, profileFrament);
                }
                transaction.show(profileFrament);
                if (forrumFragment != null){
                    transaction.hide(forrumFragment);
                }

                if (homeFragment != null){
                    transaction.hide(homeFragment);
                }
                break;

        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private int index = 0;

    private boolean isStopped = false;

    @Override
    protected void onPostResume() {
        if (isStopped) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            if (homeFragment != null) {
//                transaction.add(R.id.fragmentLayout, homeFragment);
//            }
//
//            if (profileFrament != null) {
//                transaction.add(R.id.fragmentLayout, profileFrament);
//            }
//
//            if (forrumFragment != null) {
//                transaction.add(R.id.fragmentLayout, forrumFragment);
//            }
//
//            transaction.commit();
            changeFragment(index);
            isStopped = false;
        }
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        isStopped = true;
        FragmentTransaction transaction =    getSupportFragmentManager().beginTransaction();

        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }

        if (profileFrament != null) {
            transaction.hide(profileFrament);
        }

        if (forrumFragment != null) {
            transaction.hide(forrumFragment);
        }

        transaction.commit();
        super.onPause();
    }
}