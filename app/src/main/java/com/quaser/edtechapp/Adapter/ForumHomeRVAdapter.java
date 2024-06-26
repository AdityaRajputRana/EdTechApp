package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.ViewQuestionActivity;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForumHomeRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ForumHomeRP homeRP;
    Activity activity;

    public ForumHomeRVAdapter(ForumHomeRP homeRP, Activity activity) {
        this.homeRP = homeRP;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (homeRP.getQuestions().get(position) == null){
            return 1;
        }
        return 0;
    }

    public ForumHomeRVAdapter setTag(ForumHomeRP forumHomeRP){
        homeRP = forumHomeRP;
        Log.i("TagFHP", new Gson().toJson(homeRP));
        return ForumHomeRVAdapter.this;
    }

    String loadingMessage = "Loading";
    boolean stopLoading = false;

    public void showMessage(String message, boolean stopLoading){
        loadingMessage = message;
        this.stopLoading = stopLoading;
        if (homeRP.getQuestions() == null){
            homeRP.setQuestions(new ArrayList<>());
        }
        homeRP.getQuestions().add(null);
        notifyItemInserted(homeRP.getQuestions().size()-1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_forrum, parent, false));
        else
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));

    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LoadingViewHolder){
            LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
            if (stopLoading){
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.VISIBLE);
            }
            holder.progressTxt.setText(loadingMessage);
        }else {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            QuestionRP questionRP = homeRP.getQuestions().get(position);
            holder.head.setText(questionRP.getHead());
            holder.likesTxt.setText(questionRP.getTotal_likes() + " likes");
            holder.commentsTxt.setText(questionRP.getTotal_comments() + " Answers"); //Todo show likes and comments
            if (questionRP.getImage_url() != null
                    && !questionRP.getImage_url().isEmpty()) {
                holder.imageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(questionRP.getImage_url())
                        .into(holder.imageView);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }

            if (questionRP.getTags() != null
                    && questionRP.getTags().size() > 0) {
                ForrumChipRVAdapter chipRVAdapter = new ForrumChipRVAdapter(questionRP.getTags());
                LinearLayoutManager manager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
                holder.recyclerView.setAdapter(chipRVAdapter);
                holder.recyclerView.setLayoutManager(manager);
            } else {
                holder.recyclerView.setVisibility(View.GONE);
            }

            if (questionRP.getUser_name() != null && !questionRP.getUser_name().isEmpty()) {
                holder.userNameTxt.setText(questionRP.getUser_name());
            }

            if (questionRP.getDisplay_picture() != null && !questionRP.getDisplay_picture().isEmpty()) {
                Picasso.get()
                        .load(questionRP.getDisplay_picture())
                        .transform(new CircleTransform())
                        .into(holder.userDisplayPictureImg);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ViewQuestionActivity.class);
                    intent.putExtra("hasQuestionAttached", true);
                    intent.putExtra("question", new Gson().toJson(questionRP));
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return homeRP.getQuestions().size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView progressTxt;
        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            this.progressBar = itemView.findViewById(R.id.progressBar);
            this.progressTxt = itemView.findViewById(R.id.progressText);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView head;
        RecyclerView recyclerView;
        TextView likesTxt;
        TextView commentsTxt;
        TextView userNameTxt;
        ImageView userDisplayPictureImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            head = itemView.findViewById(R.id.textView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            likesTxt = itemView.findViewById(R.id.likeTxt);
            commentsTxt = itemView.findViewById(R.id.commentsTxt);
            userDisplayPictureImg = itemView.findViewById(R.id.userDisplayImg);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            //Todo Add User Avatar
        }
    }
}
