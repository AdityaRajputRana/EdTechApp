package com.quaser.edtechapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.squareup.picasso.Picasso;

public class ViewQuestionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ViewQuestionRVAdapter(QuestionRP questionRP) {
        this.questionRP = questionRP;
    }

    public void AddAnswers(){

    }

    QuestionRP questionRP;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_rv_forrum_question, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder gHolder, int position) {
        if (gHolder instanceof QuestionViewHolder){
            QuestionViewHolder holder = (QuestionViewHolder) gHolder;

            holder.head.setText(questionRP.getHead());
            holder.body.setText(questionRP.getBody());
            holder.time.setText(questionRP.getCreatedAt());
            holder.likesTxt.setText(questionRP.getTotal_likes() + " likes");
            holder.commentsTxt.setText(questionRP.getTotal_comments() + " Answers"); //Todo show likes and comments
            if (questionRP.getImage_url() != null
            && !questionRP.getImage_url().isEmpty()){
                holder.imageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(questionRP.getImage_url())
                        .into(holder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        TextView head;
        TextView body;
        TextView time;
        ImageView imageView;
        TextView likesTxt; //Todo Add liking and tags
        TextView commentsTxt;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.headTxt);
            body = itemView.findViewById(R.id.bodyTxt);
            time = itemView.findViewById(R.id.timeTxt);
            imageView = itemView.findViewById(R.id.imageView);
            likesTxt = itemView.findViewById(R.id.likeTxt);
            commentsTxt = itemView.findViewById(R.id.commentsTxt);
        }
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder{

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            //Todo Make this
        }
    }
}
