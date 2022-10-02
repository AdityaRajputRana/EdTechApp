package com.quaser.edtechapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.RankModel;
import com.quaser.edtechapp.rest.response.RanklistRes;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardRVAdapter extends RecyclerView.Adapter {

    RanklistRes ranklistRes;
    String points = " Points";

    public LeaderboardRVAdapter(RanklistRes ranklistRes){
        this.ranklistRes = ranklistRes;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    public void showProgress(String message){
        //Todo Show Progress
    }

    public void hideProgress(String message){
        //Todo hide progress
    }

    public void hideProgress(){
        //Todo hide progress without message
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new PositionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_top, parent, false));
        else
            return new RankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PositionViewHolder){
            PositionViewHolder holder = (PositionViewHolder) viewHolder;
            if (ranklistRes.getLeaderboard() == null
            || ranklistRes.getLeaderboard().size() == 0){
                holder.firstTxt.setText("Leaderboard is not ready yet please come back later.");
                return;
            }
            ArrayList<RankModel> leaderBoard = ranklistRes.getLeaderboard();

            if (leaderBoard.get(0).getDisplay_picture() != null
                    && !leaderBoard.get(0).getDisplay_picture().isEmpty())
                 Picasso.get()
                    .load(leaderBoard.get(0).getDisplay_picture())
                    .transform(new CircleTransform())
                    .into(holder.firstImg);
            holder.firstTxt.setText("1. " +leaderBoard.get(0).getUser_name());
            holder.firstScore.setText(leaderBoard.get(0).getScore() + points);

            if (leaderBoard.size() < 2) return;
            if (leaderBoard.get(1).getDisplay_picture() != null
                    && !leaderBoard.get(1).getDisplay_picture().isEmpty())
                Picasso.get()
                    .load(leaderBoard.get(1).getDisplay_picture())
                    .transform(new CircleTransform())
                    .into(holder.secondImg);
            holder.secondTxt.setText("2. " +leaderBoard.get(1).getUser_name());
            holder.secondScore.setText(leaderBoard.get(1).getScore() + points);

            if (leaderBoard.size() < 3) return;

            if (leaderBoard.get(2).getDisplay_picture() != null
                    && !leaderBoard.get(2).getDisplay_picture().isEmpty())
                Picasso.get()
                    .load(leaderBoard.get(2).getDisplay_picture())
                    .transform(new CircleTransform())
                    .into(holder.thirdImg);
            holder.thirdTxt.setText("3. " +leaderBoard.get(2).getUser_name());
            holder.thirdScore.setText(leaderBoard.get(2).getScore() + points );

            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()
            != null && !FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString().isEmpty()){
                Picasso.get()
                        .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                        .transform(new CircleTransform())
                        .into(holder.displayImg);
            }

            if (ranklistRes.getMy_rank() != null && ranklistRes.getMy_rank().getRank() != -1){
                holder.rankTxt.setText(String.valueOf(ranklistRes.getMy_rank().getRank()));
                holder.scoreTxt.setText(ranklistRes.getMy_rank().getScore() + points);
            } else {
                holder.rankTxt.setText("-");
                holder.scoreTxt.setText("0" + points);
            }

        }
        else if (viewHolder instanceof RankViewHolder){
            RankViewHolder holder = (RankViewHolder) viewHolder;
            int index = position +2;
            RankModel rank = ranklistRes.getLeaderboard().get(index);
            holder.nameTxt.setText(rank.getUser_name());
            if (rank.getDisplay_picture() != null
             && !rank.getDisplay_picture().isEmpty()){
                Picasso.get()
                        .load(rank.getDisplay_picture())
                        .transform(new CircleTransform())
                        .into(holder.displayImg);
            }
            holder.scoreTxt.setText(rank.getScore() + points);
            holder.rankTxt.setText(rank.getRank() + ".");
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (ranklistRes.getLeaderboard() != null){
            size = ranklistRes.getLeaderboard().size();
        }
        if (size == 0 || size == 1 || size == 2 || size == 3)
            return 1;
        return size -2;
    }

    public class PositionViewHolder extends RecyclerView.ViewHolder{

        ImageView firstImg;
        ImageView secondImg;
        ImageView thirdImg;

        TextView firstTxt;
        TextView secondTxt;
        TextView thirdTxt;

        TextView firstScore;
        TextView secondScore;
        TextView thirdScore;

        TextView rankTxt;
        TextView nameTxt;
        TextView scoreTxt;
        ImageView displayImg;

        public PositionViewHolder(@NonNull View itemView) {
            super(itemView);

            firstImg = itemView.findViewById(R.id.firstImage);
            secondImg = itemView.findViewById(R.id.secondImg);
            thirdImg = itemView.findViewById(R.id.thirdImg);

            firstTxt = itemView.findViewById(R.id.firstNameTxt);
            secondTxt = itemView.findViewById(R.id.secondNameTxt);
            thirdTxt = itemView.findViewById(R.id.thirdNameTxt);

            firstScore = itemView.findViewById(R.id.firstScoreTxt);
            secondScore = itemView.findViewById(R.id.secondScoreTxt);
            thirdScore = itemView.findViewById(R.id.thirdScoreTxt);

            rankTxt = itemView.findViewById(R.id.rankNumberTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            displayImg = itemView.findViewById(R.id.displayImg);

        }
    }

    public class RankViewHolder extends RecyclerView.ViewHolder{


        TextView rankTxt;
        TextView nameTxt;
        TextView scoreTxt;
        ImageView displayImg;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);

            rankTxt = itemView.findViewById(R.id.rankNumberTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            displayImg = itemView.findViewById(R.id.displayImg);
        }
    }

}
