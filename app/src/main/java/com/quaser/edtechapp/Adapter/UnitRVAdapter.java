package com.quaser.edtechapp.Adapter;

import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.quaser.edtechapp.LiveData.UnitData;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.response.UnitRP;

import org.w3c.dom.Text;

public class UnitRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    UnitRP unitRP;
    Activity activity;
    MutableLiveData<UnitRP> mutableLiveData;

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
                notifyItemChanged(0);
                notifyItemChanged(changedItem);
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
                if (previousLesson.isIs_complete()){
                    holder.topProgress.setBackgroundColor(activity.getResources()
                            .getColor(R.color.color_accent1_blue));
                    holder.indicatorImg.setImageDrawable(activity.getDrawable(R.drawable.ic_active_ball));
                }
            }
            ShortLesson lesson = unitRP.getLesson().get(position);
            if (lesson.isIs_complete()){
                holder.topProgress.setBackgroundColor(activity.getResources()
                        .getColor(R.color.color_accent1_blue));
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
