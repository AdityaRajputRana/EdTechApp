package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.Answer;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.Method;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewQuestionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean haveAnswers = true;
    boolean isLoadingAnswers = true;

    ArrayList<Answer> mAnswers = new ArrayList<Answer>();
    
    Context context;

    public void showNoAnswerYetTxt(){
        haveAnswers = false;
        isLoadingAnswers = false;
        notifyItemChanged(0);
    }

    public ViewQuestionRVAdapter(QuestionRP questionRP, Context context) {
        this.questionRP = questionRP;
        this.context = context;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder gHolder, int pos) {
        final int positionf = pos;
        if (gHolder instanceof QuestionViewHolder){
            QuestionViewHolder holder = (QuestionViewHolder) gHolder;

            holder.head.setText(questionRP.getHead());
            holder.body.setText(questionRP.getBody());
            holder.time.setText(questionRP.getCreatedAt());
            holder.likesTxt.setText(questionRP.getTotal_likes() + " likes");
            if (questionRP.isIs_liked())
                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
            else
                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

            holder.commentsTxt.setText(questionRP.getTotal_comments() + " Answers"); //Todo show likes and comments
            if (questionRP.getImage_url() != null
            && !questionRP.getImage_url().isEmpty()){
                holder.imageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(questionRP.getImage_url())
                        .into(holder.imageView);
            }

            holder.likesTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionRP.setIs_liked(!questionRP.isIs_liked());
                    if (questionRP.isIs_liked())
                        holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                    else
                        holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    APIMethods.likeQuestion(questionRP.get_id(), new APIResponseListener<QuestionRP>() {
                        @Override
                        public void success(QuestionRP response) {
                            questionRP.setTotal_likes(questionRP.getTotal_likes()+1);
                            notifyItemChanged(positionf);
                            if (response != null) {
                                if (response.isIs_liked())
                                    holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                                else
                                    holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                            }
                        }

                        @Override
                        public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                            questionRP.setIs_liked(!questionRP.isIs_liked());
                            if (questionRP.isIs_liked())
                                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                            else
                                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                            if (context instanceof Activity)
                            Method.showFailedAlert(context, code + " - "+  message);
                            else
                                Toast.makeText(context, code + " - "+  message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            if (isLoadingAnswers){
                holder.progressBar.setVisibility(View.VISIBLE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
            }

            if (!haveAnswers){
                holder.noAnsTxt.setVisibility(View.VISIBLE);
            }
        } else if (gHolder instanceof AnswerViewHolder){
            int position = positionf-1;
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
