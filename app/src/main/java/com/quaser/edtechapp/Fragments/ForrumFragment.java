package com.quaser.edtechapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.ForumHomeRVAdapter;
import com.quaser.edtechapp.Adapter.TagsRVAdapter;
import com.quaser.edtechapp.AddQuestionActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.SearchActivity;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.rest.response.TagsRP;
import com.quaser.edtechapp.utils.Method;
import com.quaser.edtechapp.utils.Statics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ForrumFragment extends Fragment {
    ExtendedFloatingActionButton addBtn;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    EditText searchEt;
    ForumHomeRVAdapter adapter;

    public static ForrumFragment forrumFragment;

    public static HashMap<String, String> forrums= new HashMap<String, String>();

    public ForrumFragment() {
        // Required empty public constructor
    }

    public static ForrumFragment newInstance(){
        if (forrumFragment == null){
            forrumFragment = new ForrumFragment();
        }
        return forrumFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forrum, container, false);
        findViews(view);
        fetchForumQuestions();
        setUpTags();
        return view;
    }

    RecyclerView tagsRv;
    ArrayList<String> tagsList;
    private void setUpTags() {
        Statics.getTagsList(new Statics.TagsListener() {
            @Override
            public void onResult(boolean isSuccess, String message, ArrayList<String> tags) {
                if (!isSuccess){
                    Method.showFailedAlert(getActivity(), message);
                } else {
                    tagsList = tags;
                    showTagsRV();
                }
            }
        });
    }

    TagsRVAdapter tagsAdapter;

    private void showTagsRV() {
        tagsAdapter = new TagsRVAdapter("All", tagsList, getActivity(), new TagsRVAdapter.Listener() {
            @Override
            public void selectSubCat(String tag) {


                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                if (forrums.containsKey(tag)) {
                    ForumHomeRP filtered = new Gson().fromJson(forrums.get(tag), ForumHomeRP.class);
                    adapter = adapter.setTag(filtered);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (filtered.getQuestions() == null || filtered.getQuestions().size() == 0){
                        adapter.showMessage("No Questions under '" + tag + "' category.\nYou can explore other categories.", true);
                    }
                } else {
                    APIMethods.getForumQuestion(new APIResponseListener<ForumHomeRP>() {
                        @Override
                        public void success(ForumHomeRP response) {
                            forrums.put(tag, new Gson().toJson(response));
                            if (response.areMorePagesAvailable()) {
                                response.getQuestions().add(null);
                            }

                            adapter = adapter.setTag(new Gson().fromJson(forrums.get(tag), ForumHomeRP.class));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            if (response.getQuestions() == null || response.getQuestions().size() == 0){
                                adapter.showMessage("No Questions under '" + tag + "' category.\nYou can explore other categories.", true);
                            }
                        }

                        @Override
                        public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                            Method.showFailedAlert(getActivity(), code + "-" + message);
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    tagsAdapter.resetToAll();
                        }
                    }, tag);
                }
            }
                //Todo: Fetch store and maintain here, progress bar, thread and filter from all questions list and then show them.
        });
         LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
         tagsRv.setAdapter(tagsAdapter);
         tagsRv.setLayoutManager(manager);
         tagsRv.setVisibility(View.VISIBLE);

    }

    private void fetchForumQuestions() {
        APIMethods.getForumQuestion(getActivity(), new APIResponseListener<ForumHomeRP>() {
            @Override
            public void success(ForumHomeRP response) {
                progressBar.setVisibility(View.GONE);
                forrums.put("All", new Gson().toJson(response));
                showRecyclerView(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), code+"-"+message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showRecyclerView(ForumHomeRP res) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        if (res.areMorePagesAvailable()){
            res.getQuestions().add(null);
        }
        adapter = new ForumHomeRVAdapter(res, getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if ((visibleItem + firstVisibleItem) >= totalItem
                        && firstVisibleItem >= 0){
                    res.paginate(adapter, getActivity());
                    if (!res.areMorePagesAvailable()){
                        recyclerView.removeOnScrollListener(this);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);

        if (res.getQuestions() == null || res.getQuestions().size() == 0){
            adapter.showMessage("No Questions have been posted to our forum. To Add a question click (+) button below.", true);
        }
    }

    private void findViews(View view) {
        addBtn = view.findViewById(R.id.addBtn);
        searchEt = view.findViewById(R.id.searchEt);
        recyclerView = view.findViewById(R.id.questionRV);
        progressBar = view.findViewById(R.id.progressBar);
        tagsRv = view.findViewById(R.id.tagsSelectionRv);
        addListeners();
    }

    private void addListeners() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddActivity();
            }
        });

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!searchEt.getText().toString().isEmpty()){
                        performSearch();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("KEYWORD", searchEt.getText().toString());
        getActivity().startActivity(intent);
    }

    private void launchAddActivity() {
        Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
        startActivity(intent);
    }
}