package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ForumHomeRVAdapter extends RecyclerView.Adapter<ForumHomeRVAdapter.MyViewHolder> {

    ForumHomeRP homeRP;
    Activity activity;

    public ForumHomeRVAdapter(ForumHomeRP homeRP, Activity activity) {
        this.homeRP = homeRP;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_forrum, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuestionRP questionRP = homeRP.getQuestions().get(position);
        holder.head.setText(questionRP.getHead());
        holder.likesTxt.setText(questionRP.getTotal_likes() + " likes");
        holder.commentsTxt.setText(questionRP.getTotal_comments() + " Answers"); //Todo show likes and comments
        if (questionRP.getImage_url() != null
                && !questionRP.getImage_url().isEmpty()){
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(questionRP.getImage_url())
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        if (questionRP.getTags() != null
        && questionRP.getTags().size() >0){
            ForrumChipRVAdapter chipRVAdapter = new ForrumChipRVAdapter(questionRP.getTags());
            LinearLayoutManager manager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
            holder.recyclerView.setAdapter(chipRVAdapter);
            holder.recyclerView.setLayoutManager(manager);
        } else {
            holder.recyclerView.setVisibility(View.GONE);
        }

        if (questionRP.getUser_name() != null && !questionRP.getUser_name().isEmpty()){
            holder.userNameTxt.setText(questionRP.getUser_name());
        }

        if (questionRP.getDisplay_picture() != null && !questionRP.getDisplay_picture().isEmpty()){
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

    @Override
    public int getItemCount() {
        return homeRP.getQuestions().size();
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
