package com.quaser.edtechapp.Adapter;

import static com.quaser.edtechapp.utils.WsyswigUtils.getContentface;
import static com.quaser.edtechapp.utils.WsyswigUtils.getHeadingTypeface;
import static com.quaser.edtechapp.utils.WsyswigUtils.renderBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.AddAnswerActivity;
import com.quaser.edtechapp.AddQuestionActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.Answer;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.Method;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.quaser.edtechapp.utils.WsyswigUtils;
import com.quaser.edtechapp.wsywig.Editor;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

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
            renderBody(holder.body, questionRP.getBody(), context);
            holder.time.setText(questionRP.getCreatedAt());
            holder.likesTxt.setText(questionRP.getTotal_likes() + " likes");
            if (questionRP.isIs_liked())
                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
            else
                holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

            holder.commentsTxt.setText(questionRP.getTotal_comments() + " Answers"); //Todo show likes and comments

            if (questionRP.getDisplay_picture() != null && !questionRP.getDisplay_picture().isEmpty())
                Picasso.get()
                        .load(questionRP.getDisplay_picture())
                        .transform(new CircleTransform())
                        .into(holder.userDisplayImg);

            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null){
                Picasso.get()
                        .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .transform(new CircleTransform())
                        .into(holder.mDisplayImage);
            }

            if (questionRP.getUser_name() != null && !questionRP.getUser_name().isEmpty())
                holder.userDisplayName.setText(questionRP.getUser_name());

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
                            if (response != null) {
                                if (response.isIs_liked()) {
                                    questionRP.setTotal_likes(questionRP.getTotal_likes() + 1);
                                    holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                                } else {
                                    holder.likesTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                                    questionRP.setTotal_likes(questionRP.getTotal_likes()-1);
                                }
                                notifyItemChanged(positionf);
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

            holder.answerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddAnswerActivity.class);
                    intent.putExtra("QID", questionRP.get_id());
                    intent.putExtra("QHEAD", questionRP.getHead());
                    context.startActivity(intent);
                }
            });
        } else if (gHolder instanceof AnswerViewHolder){
            int position = positionf-1;
            AnswerViewHolder holder = (AnswerViewHolder) gHolder;
            Answer answer = mAnswers.get(position);
            int totalUps = 0;
                totalUps = answer.getTotal_upvotes();

            WsyswigUtils.renderBody(holder.body, answer.getBody(), context);
            holder.upvotesTxt.setText(totalUps + " upvotes");

            if (answer.isIs_liked())
                holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
            else
                holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);


            if (answer.getDisplay_picture() != null && !answer.getDisplay_picture().isEmpty())
                Picasso.get()
                        .load(answer.getDisplay_picture())
                        .transform(new CircleTransform())
                        .into(holder.userDisplayImg);
            if (answer.getUser_name() != null && !answer.getUser_name().isEmpty())
                holder.userDisplayName.setText(answer.getUser_name());


            holder.upvotesTxt.setOnClickListener(view -> {
                answer.setIs_liked(!answer.isIs_liked());
                if (answer.isIs_liked())
                    holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
                else
                    holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);

                APIMethods.upVoteAnswer(questionRP.get_id(), answer.get_id(), new APIResponseListener<Answer>() {
                    @Override
                    public void success(Answer response) {
                        if (response != null) {
                            Answer answerNew = mAnswers.get(position);
                            answerNew.setIs_liked(response.isIs_liked());
                            if (response.isIs_liked()) {
                                Log.i("Lesson", "liked");
                                answerNew.setTotal_upvotes(answerNew.getTotal_upvotes() + 1);
                                Log.i("Lesson", "likes: " + answerNew.getTotal_upvotes());
                                holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
                            } else {
                                Log.i("Lesson", "unliked");
                                Log.i("Lesson", "likes: " + answerNew.getTotal_upvotes());
                                holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);
                                answerNew.setTotal_upvotes(answerNew.getTotal_upvotes()-1);
                            }
                            notifyItemChanged(positionf);
                        }
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        answer.setIs_liked(!answer.isIs_liked());
                        if (answer.isIs_liked())
                            holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
                        else
                            holder.upvotesTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);

                        Method.showFailedAlert(context, code + " - "+  message);
                    }
                });


            });

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
        Editor body;
        TextView time;
        TextView likesTxt; //Todo Add liking and tags
        TextView commentsTxt;

        ProgressBar progressBar;
        TextView noAnsTxt;
        
        LinearLayout answerLayout;
        ImageView userDisplayImg;
        TextView userDisplayName;

        ImageView mDisplayImage;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.headTxt);
            body = itemView.findViewById(R.id.bodyTxt);
            time = itemView.findViewById(R.id.timeTxt);
            likesTxt = itemView.findViewById(R.id.likeTxt);
            commentsTxt = itemView.findViewById(R.id.commentsTxt);

            noAnsTxt= itemView.findViewById(R.id.endText);
            progressBar = itemView.findViewById(R.id.progressBar);
            answerLayout = itemView.findViewById(R.id.answerLayout);

            userDisplayImg = itemView.findViewById(R.id.userDisplayImg);
            userDisplayName = itemView.findViewById(R.id.userNameTxt);
            mDisplayImage = itemView.findViewById(R.id.mDisplayPicture);
        }
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder{

        TextView upvotesTxt;
        Editor body;

        ImageView userDisplayImg;
        TextView userDisplayName;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            body = itemView.findViewById(R.id.bodyTxt);
            upvotesTxt = itemView.findViewById(R.id.likeTxt);
            userDisplayImg = itemView.findViewById(R.id.userDisplayImg);
            userDisplayName = itemView.findViewById(R.id.userNameTxt);
        }
    }
}
