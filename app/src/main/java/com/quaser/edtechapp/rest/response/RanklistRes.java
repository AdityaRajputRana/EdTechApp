package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.RankModel;

import java.util.ArrayList;

public class RanklistRes {
    RankModel my_rank;
    ArrayList<RankModel> leaderboard;
    int page;
    int limit;
    int pages;
    int total;

    public boolean isLoading = false;

    public RankModel getMy_rank() {
        return my_rank;
    }

    public ArrayList<RankModel> getLeaderboard() {
        return leaderboard;
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

    public void paginate(){
        //show pb
        //
    }
}
