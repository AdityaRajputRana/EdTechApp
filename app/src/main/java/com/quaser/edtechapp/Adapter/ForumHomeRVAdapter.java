package com.quaser.edtechapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.squareup.picasso.Picasso;

public class ForumHomeRVAdapter extends RecyclerView.Adapter<ForumHomeRVAdapter.MyViewHolder> {

    ForumHomeRP homeRP;

    public ForumHomeRVAdapter(ForumHomeRP homeRP) {
        this.homeRP = homeRP;
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            head = itemView.findViewById(R.id.textView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            likesTxt = itemView.findViewById(R.id.likeTxt);
            commentsTxt = itemView.findViewById(R.id.commentsTxt);

            //Todo Add User Avatar
        }
    }
}
