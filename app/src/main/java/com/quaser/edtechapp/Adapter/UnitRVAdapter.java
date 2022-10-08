package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.quaser.edtechapp.FrameActivity;
import com.quaser.edtechapp.LiveData.UnitData;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.response.AssignmentListRP;
import com.quaser.edtechapp.rest.response.UnitRP;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class UnitRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    UnitRP unitRP;
    Activity activity;
    MutableLiveData<UnitRP> mutableLiveData;

    ArrayList<ShortLesson> filteredLessons = new ArrayList<ShortLesson>();
    int firstFilteredLesson = 0;
    private void filterLessons(){
        filteredLessons = new ArrayList<ShortLesson>();
        firstFilteredLesson = unitRP.getLesson().size();
        boolean gotActive = false;
        for (int i = 0; i < unitRP.getLesson().size(); i++){
            if (filteredLessons.size() >3){
                break;
            }
            if (gotActive){
                filteredLessons.add(unitRP.getLesson().get(i));
            } else {
                if (!unitRP.getLesson().get(i).isIs_complete()){
                    firstFilteredLesson = i;
                    if (i>0) {
                        filteredLessons.add(unitRP.getLesson().get(i - 1));
                        firstFilteredLesson = i-1;
                    }
                    filteredLessons.add(unitRP.getLesson().get(i));
                    gotActive = true;
                }
            }
        }
        while (filteredLessons.size() < 4){
            if (firstFilteredLesson == 0){
                break;
            } else {
                firstFilteredLesson--;
                Collections.reverse(filteredLessons);
                filteredLessons.add(unitRP.getLesson().get(firstFilteredLesson));
                Collections.reverse(filteredLessons);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new UnitViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit_header, parent, false));
        } else if (viewType == 1)
            return new LessonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit_lesson, parent, false));
        else if (viewType == 2)
            return new ViewAllViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit_view_all, parent, false));
        else if (viewType == 3)
            return new AssignmentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit_assignments, parent, false));
        else
            return new AdditionalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit_additionals, parent, false));
    }

    public UnitRVAdapter(UnitRP unitRP, Activity activity) {
        this.unitRP = unitRP;
        if (unitRP.getLesson() != null){
            filterLessons();
        }
//        UnitData.setUnitData(unitRP);
        mutableLiveData = UnitData.getUnitRPMutableLiveData();
        this.activity = activity;
        setObserver();
        Log.i("eta unitrp", new Gson().toJson(unitRP));
    }

    private void setObserver() {
        mutableLiveData.observe((LifecycleOwner) activity, new Observer<UnitRP>() {
            @Override
            public void onChanged(UnitRP newUnit) {
                int changedItem = unitRP.getCompleted_lessons();
                unitRP = newUnit;
                filterLessons();
                notifyItemChanged(0);
                if (rvState == 0){
                    int k = Math.min(unitRP.getLesson().size(), 4);
                    notifyItemRangeChanged(1, 4);
                } else {
                    notifyItemRangeChanged(1, unitRP.getLesson().size());
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {
        if (mHolder instanceof UnitViewHolder){
            UnitViewHolder holder = (UnitViewHolder) mHolder;

            holder.titleTxt.setText(unitRP.getUnit_title());
            Log.i("eta units", String.valueOf(unitRP.getTotal_lessons()));
            if (unitRP.getTotal_lessons() == 0){
                unitRP.setTotal_lessons(1);
                unitRP.setCompleted_lessons(1);
            }

            int percentCompleted = unitRP.getCompleted_lessons()*100/unitRP.getTotal_lessons();
            holder.progressBar.setProgress(percentCompleted);

            if (unitRP.getLesson() != null
            && unitRP.getLesson().size() >0
                    && unitRP.getLesson().get(0).isIs_complete()){
                holder.bottomProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
            }

            if (unitRP.isHas_user_started()){
                holder.bottomProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
            }
        } else if (mHolder instanceof LessonViewHolder){
            LessonViewHolder holder = (LessonViewHolder) mHolder;
            position = position-1;
            if (position == 0 && unitRP.isHas_user_started()){
                holder.topProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
                holder.indicatorImg.setImageDrawable(activity.getDrawable(R.drawable.ic_active_ball));
            } else if (position > 0){
                ShortLesson previousLesson = unitRP.getLesson().get(position-1);
                if (rvState == 0){
                    previousLesson = filteredLessons.get(position-1);
                }
                if (previousLesson.isIs_complete()){
                    holder.topProgress.setBackgroundColor(activity.getResources()
                            .getColor(R.color.color_accent1_blue));
                    holder.indicatorImg.setImageDrawable(activity.getDrawable(R.drawable.ic_active_ball));
                    holder.titleTxt.setTextColor(activity.getColor(R.color.color_accent1_blue));
                }
            }
            ShortLesson lesson = unitRP.getLesson().get(position);
            if (rvState == 0){
                lesson = filteredLessons.get(position);
            }
            if (lesson.isIs_complete()){
                holder.topProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
                holder.titleTxt.setTextColor(activity.getColor(R.color.color_secondary_txt));
                holder.indicatorImg.setImageDrawable(activity.getDrawable(R.drawable.ic_tick));
                holder.bottomProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
            }

            holder.titleTxt.setText(lesson.getName());
            if (lesson.getShort_description() != null && !lesson.getShort_description().isEmpty()){
                holder.bodyTxt.setText(lesson.getShort_description());
            } else {
                holder.bodyTxt.setVisibility(View.GONE);
            }

            int mRealIndex = unitRP.getLesson().indexOf(lesson);
            if (mRealIndex+1>= unitRP.getLesson().size())
                holder.bottomProgress.setVisibility(View.GONE);
            else
                holder.bottomProgress.setVisibility(View.VISIBLE);
        }
        else if (mHolder instanceof ViewAllViewHolder){
            ViewAllViewHolder holder = (ViewAllViewHolder) mHolder;
            if (rvState == 0){
                holder.viewAllBtn.setText("View All");
                holder.viewAllBtn.setOnClickListener(view -> {
                    rvState =1;
//                    notifyItemRangeChanged(1, 4);
//                    notifyItemRangeInserted(5, unitRP.getLesson().size()-4);
//                    notifyItemChanged(unitRP.getLesson().size()+1);
                    if (firstFilteredLesson != 0)
                        notifyItemRangeInserted(1, firstFilteredLesson);
                    if (unitRP.getLesson().size() - firstFilteredLesson - 4 > 0)
                        notifyItemRangeInserted(firstFilteredLesson+5, unitRP.getLesson().size() - firstFilteredLesson - 4);
                    notifyItemChanged(unitRP.getLesson().size()+1);

                });
            } else {
                holder.viewAllBtn.setText("View Less");
                holder.viewAllBtn.setOnClickListener(view -> {
                    rvState =0;
//                    notifyItemRangeChanged(1, 4);
//                    notifyItemRangeRemoved(5, unitRP.getLesson().size()-4);
//                    notifyItemChanged(5);

                    if (firstFilteredLesson != 0)
                        notifyItemRangeRemoved(1, firstFilteredLesson);
                    if (unitRP.getLesson().size() - firstFilteredLesson - 4 > 0)
                        notifyItemRangeRemoved(5, unitRP.getLesson().size() - firstFilteredLesson - 4);
                    notifyItemChanged(5);

                });
            }
        }
        else if (mHolder instanceof AssignmentsViewHolder){
            AssignmentsViewHolder holder = (AssignmentsViewHolder) mHolder;
            AssignmentListRP rp = new AssignmentListRP(unitRP.getAssignments());
            AssignmentsRVAdapter adapter = new AssignmentsRVAdapter(rp, activity);
            LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setLayoutManager(manager);
        }
        else if (mHolder instanceof AdditionalViewHolder){
            AdditionalViewHolder holder = (AdditionalViewHolder) mHolder;

            int alpha, beta, gama;
            alpha = beta = gama = 0;

            if (unitRP.getLesson() != null)
                alpha = unitRP.getLesson().size();
            if (unitRP.getAssignments() != null) //Todo get this from api
                beta = unitRP.getAssignments().size();
            if (unitRP.getAdditionals() != null)
                gama = unitRP.getAdditionals().size();

            int k = Math.min(alpha, 4);
            if (rvState == 1){
                k = alpha;
            }

            int p = position - 1 - k - (alpha>4?1:0) - (beta>0?1:0);

            if (p==0){
                holder.additionalTitleTxt.setVisibility(View.VISIBLE);
            } else
                holder.additionalTitleTxt.setVisibility(View.GONE);

            ArrayList<ShortLesson> adsnl = unitRP.getAdditionals();

            int index = p*2;

            holder.titleTxt1.setText(adsnl.get(index).getName());
            holder.typeTxt1.setText(adsnl.get(index).getType().substring(0, 1).toUpperCase(Locale.ROOT)
            + adsnl.get(index).getType().substring(1));

            int rid = R.drawable.ic_baseline_play_lesson_24;
            switch (adsnl.get(index).getType()){
                case "video":
                    rid = R.drawable.ic_baseline_play_arrow_24;
                    break;
                case "test":
                    rid = R.drawable.ic_baseline_question_answer_24;
                    break;
                case "article":
                    rid = R.drawable.ic_baseline_article_24;
                    break;
                case "assignment":
                    rid = R.drawable.ic_lesson_assignment;
                    break;
            }
            holder.iconImg1.setImageDrawable(activity.getDrawable(rid));
            ShortLesson s1 = adsnl.get(p);
            holder.layout1.setOnClickListener(view -> {
                Intent intent = new Intent(activity, FrameActivity.class);
                intent.putExtra("type", "SHORT_LESSON");
                intent.putExtra("SHORT_LESSON", new Gson().toJson(s1));
                activity.startActivity(intent);
            });

            index++;
            if (index >= adsnl.size()){
                holder.layout2.setVisibility(View.GONE);
            } else {
                holder.layout2.setVisibility(View.VISIBLE);
                holder.titleTxt2.setText(adsnl.get(index).getName());
                holder.typeTxt2.setText(adsnl.get(index).getType().substring(0, 1).toUpperCase(Locale.ROOT)
                        + adsnl.get(index).getType().substring(1));

                rid = R.drawable.ic_baseline_play_lesson_24;
                switch (adsnl.get(index).getType()){
                    case "video":
                        rid = R.drawable.ic_baseline_play_arrow_24;
                        break;
                    case "test":
                        rid = R.drawable.ic_baseline_question_answer_24;
                        break;
                    case "article":
                        rid = R.drawable.ic_baseline_article_24;
                        break;
                    case "assignment":
                        rid = R.drawable.ic_lesson_assignment;
                        break;
                }
                holder.iconImg2.setImageDrawable(activity.getDrawable(rid));
                ShortLesson s2 = adsnl.get(p);
                holder.layout2.setOnClickListener(view -> {
                    Intent intent = new Intent(activity, FrameActivity.class);
                    intent.putExtra("type", "SHORT_LESSON");
                    intent.putExtra("unit_id", unitRP.get_id());
                    intent.putExtra("SHORT_LESSON", new Gson().toJson(s2));
                    activity.startActivity(intent);
                });
            }
        }
    }


    int rvState = 0; //0- Shrinked, 1 - All Lessons
    @Override
    public int getItemCount() {
        int alpha, beta, gama;
        alpha = beta = gama = 0;

        if (unitRP.getLesson() != null)
            alpha = unitRP.getLesson().size();
        if (unitRP.getAssignments() != null) //Todo get this from api
            beta = unitRP.getAssignments().size();
        if (unitRP.getAdditionals() != null)
            gama = unitRP.getAdditionals().size();

        if (rvState == 0){
            return( 1 + Math.min(alpha, 4) + (alpha > 4?1:0) + (beta>0?1:0) + (int) (gama/2) + (gama%2));
        } else {
            return( 1 + alpha + (alpha > 4?1:0) + (beta>0?1:0) + (int) (gama/2) + (gama%2));
        }
    }

    @Override
    public int getItemViewType(int p) {

        int alpha, beta, gama;
        alpha = beta = gama = 0;

        if (unitRP.getLesson() != null)
            alpha = unitRP.getLesson().size();
        if (unitRP.getAssignments() != null) //Todo get this from api
            beta = unitRP.getAssignments().size();
        if (unitRP.getAdditionals() != null)
            gama = unitRP.getAdditionals().size();

        int k = Math.min(alpha, 4);
        if (rvState == 1){
            k = alpha;
        }


        if (p==0)
            return 0;
        if (p <= k)
            return 1;
        if (p==(k+1) && alpha >4)
            return 2;
        if ((beta > 0) && (p<= (k+1+(alpha>4?1:0))))
            return 3;

        return 4;
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        CircularProgressIndicator progressBar;
        LinearLayout bottomProgress;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            progressBar = itemView.findViewById(R.id.progressBar);
            bottomProgress = itemView.findViewById(R.id.bottomProgress);
        }
    }

    public class ViewAllViewHolder extends RecyclerView.ViewHolder{
        MaterialButton viewAllBtn;

        public ViewAllViewHolder(@NonNull View itemView) {
            super(itemView);
            viewAllBtn = itemView.findViewById(R.id.viewAllBtn);
        }
    }
    public class AssignmentsViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;

        public AssignmentsViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.assignmentRv);
        }
    }

    public class AdditionalViewHolder extends RecyclerView.ViewHolder{

        TextView additionalTitleTxt;

        ConstraintLayout layout1;
        TextView titleTxt1;
        TextView typeTxt1;
        ImageView iconImg1;

        ConstraintLayout layout2;
        TextView titleTxt2;
        TextView typeTxt2;
        ImageView iconImg2;

        public AdditionalViewHolder(@NonNull View itemView) {
            super(itemView);
            additionalTitleTxt = itemView.findViewById(R.id.additionalTitle);

            layout1 = itemView.findViewById(R.id.layout1);
            layout2 = itemView.findViewById(R.id.layout2);
            titleTxt1 = itemView.findViewById(R.id.titleTxt1);
            titleTxt2 = itemView.findViewById(R.id.titleTxt2);
            typeTxt1 = itemView.findViewById(R.id.typeTxt1);
            typeTxt2 = itemView.findViewById(R.id.typeTxt2);
            iconImg1 = itemView.findViewById(R.id.iconImg1);
            iconImg2= itemView.findViewById(R.id.iconImg2);
        }
    }



    public class LessonViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        TextView bodyTxt;
        ImageView indicatorImg;
        LinearLayout bottomProgress;
        LinearLayout topProgress;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleTxt);
            bodyTxt = itemView.findViewById(R.id.bodyTxt);
            bottomProgress = itemView.findViewById(R.id.bottomProgress);
            indicatorImg = itemView.findViewById(R.id.progressBar);
            topProgress = itemView.findViewById(R.id.topProgress);
        }
    }
}
