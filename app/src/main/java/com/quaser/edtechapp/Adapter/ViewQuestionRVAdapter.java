package com.quaser.edtechapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.Answer;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewQuestionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean haveAnswers = true;
    boolean isLoadingAnswers = true;

    ArrayList<Answer> mAnswers = new ArrayList<Answer>();

    public void showNoAnswerYetTxt(){
        haveAnswers = false;
        isLoadingAnswers = false;
        notifyItemChanged(0);
    }

    public ViewQuestionRVAdapter(QuestionRP questionRP) {
        this.questionRP = questionRP;
    }

    public void AddAnswers(){

    }

    QuestionRP questionRP;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_rv_forrum_question, parent, false
            ));
        return new AnswerViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_rv_forrum_answer, parent, false
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

            if (isLoadingAnswers){
                holder.progressBar.setVisibility(View.VISIBLE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
            }

            if (!haveAnswers){
                holder.noAnsTxt.setVisibility(View.VISIBLE);
            }
        } else if (gHolder instanceof AnswerViewHolder){
            position = position-1;
            AnswerViewHolder holder = (AnswerViewHolder) gHolder;
            Answer answer = mAnswers.get(position);
            int totalUps = 0;
            if (answer.getUpvotes() != null){
                totalUps = answer.getUpvotes().size();
            } else {
                totalUps = answer.getTotal_upvotes();
            }

            holder.body.setText(answer.getBody());
            holder.upvotesTxt.setText(totalUps + " upvotes");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return 1 + mAnswers.size();
    }

    public void addAnswers(ArrayList<Answer> answers) {
        if (answers == null || answers.size() ==0){
            if (mAnswers.size() ==0){
                showNoAnswerYetTxt();
                return;
            }
        }

        int positionStart = mAnswers.size();

        mAnswers.addAll(answers);
        isLoadingAnswers = false;
        haveAnswers = true;
        notifyDataSetChanged();
//        notifyItemChanged(0);
//        notifyItemRangeInserted(positionStart, answers.size());
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        TextView head;
        TextView body;
        TextView time;
        ImageView imageView;
        TextView likesTxt; //Todo Add liking and tags
        TextView commentsTxt;

        ProgressBar progressBar;
        TextView noAnsTxt;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.headTxt);
            body = itemView.findViewById(R.id.bodyTxt);
            time = itemView.findViewById(R.id.timeTxt);
            imageView = itemView.findViewById(R.id.imageView);
            likesTxt = itemView.findViewById(R.id.likeTxt);
            commentsTxt = itemView.findViewById(R.id.commentsTxt);

            noAnsTxt= itemView.findViewById(R.id.endText);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder{

        TextView upvotesTxt;
        TextView body;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            body = itemView.findViewById(R.id.bodyTxt);
            upvotesTxt = itemView.findViewById(R.id.likeTxt);
        }
    }
}
