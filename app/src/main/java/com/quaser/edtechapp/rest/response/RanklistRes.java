package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.Adapter.LeaderboardRVAdapter;
import com.quaser.edtechapp.models.RankModel;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;

import java.util.ArrayList;

public class RanklistRes {
    RankModel user;
    ArrayList<RankModel> result;
    int page;
    int limit;
    int pages;
    int total;
    int position;

    public boolean isLoading = false;

    public RankModel getMy_rank() {
        return user;
    }

    public ArrayList<RankModel> getLeaderboard() {
        return result;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getPages() {
        return pages;
    }

    public int getTotal() {
        return total;
    }

    public RanklistRes() {
    }

    public boolean areMorePages(){
       return page<pages;
    }

    public void paginate(LeaderboardRVAdapter adapter){
        if (!isLoading){
            isLoading = true;
            adapter.showProgress("Loading more ranks");

            APIResponseListener<RanklistRes> responseListener = new APIResponseListener<RanklistRes>() {
                @Override
                public void success(RanklistRes response) {

                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {

                }
            };
        }
    }
}
