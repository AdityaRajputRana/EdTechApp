package com.quaser.edtechapp.rest.response;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.ForumHomeRVAdapter;
import com.quaser.edtechapp.Fragments.ForrumFragment;
import com.quaser.edtechapp.models.Filter;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.utils.Method;

import java.util.ArrayList;

public class ForumHomeRP {
    ArrayList<QuestionRP> questions;
    private int page;
    private int pages;
    private int count;

    public Filter getFilter() {
        return filter;
    }

    Filter filter;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getCount() {
        return count;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private boolean isLoading = false;

    public ArrayList<QuestionRP> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionRP> questions) {
        this.questions = questions;
    }

    public ForumHomeRP() {

    }


    public void paginate(RecyclerView.Adapter adapter, Context context ){
        if (areMorePagesAvailable()){

            APIResponseListener<ForumHomeRP> apiListener = new APIResponseListener<ForumHomeRP>() {
                @Override
                public void success(ForumHomeRP mRes) {
                    page = mRes.getPage();
                    int startingIndex = count;
                    if (questions.size() > count && questions.get(count) == null) {
                        questions.remove(count);
                        adapter.notifyItemRemoved(count);
                    }
                    count = count + mRes.getCount();
                    questions.addAll(mRes.getQuestions());
                    pages = mRes.getPages();
                    if (mRes.getFilter() != null) {
                        filter = mRes.getFilter();
                    }
                    isLoading = false;

                    if (adapter instanceof ForumHomeRVAdapter) {
                        ForumHomeRVAdapter forumHomeRVAdapter = (ForumHomeRVAdapter) adapter;
                        if (areMorePagesAvailable()) {
                            forumHomeRVAdapter.showMessage("Loading more questions", false);
                        } else {
                            if (!isLoading){
                                forumHomeRVAdapter.showMessage("No more questions to display", true);
                            }
                        }
                    }
                    adapter.notifyItemRangeInserted(startingIndex, mRes.getCount());
                    if (ForrumFragment.forrums != null){
                        String tag = "All";
                        if (filter != null && filter.isTagged()){
                            tag = filter.getTag();
                        }
                        ForrumFragment.forrums.put(tag, new Gson().toJson(ForumHomeRP.this));
                    }
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    isLoading = false;
                    if (questions.size() > count && questions.get(count) == null){
                        questions.remove(count);
                        adapter.notifyItemRemoved(count);
                    }
                    Method.showFailedAlert(context, code + " - " + message);
                }
            };

            isLoading =true;
            if (filter == null
            || filter.getKeyword() == null
            || filter.getKeyword().isEmpty()) {
                if (filter != null && filter.isTagged()){
                    APIMethods.getForumQuestion(page+1, apiListener, filter.getTag());
                }else {
                    APIMethods.getForumQuestion(page + 1, apiListener);
                }
            } else {
                APIMethods.getForumQuestion(filter.getKeyword(), page+1, apiListener);
            }
        }
    }

    public boolean areMorePagesAvailable(){
        if (pages > page && !isLoading)
            return true;
        else
            return false;
    }
}
