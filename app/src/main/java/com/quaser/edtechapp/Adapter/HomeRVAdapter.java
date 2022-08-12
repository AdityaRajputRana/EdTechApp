package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortUnit;
import com.quaser.edtechapp.rest.response.HomeRP;

public class HomeRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    HomeRP homeRP;
    Activity activity;

    public HomeRVAdapter(HomeRP homeRP, Activity activity) {
        this.homeRP = homeRP;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_home, parent, false));
        }
        return new UnitViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_unit, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder gHolder, int position) {
        position = position - 1;
        if (gHolder instanceof CourseViewHolder){
            CourseViewHolder holder = (CourseViewHolder) gHolder;
            String nameTxt = "Hello, " + homeRP.getName() +"!";
            holder.nameTxt.setText(nameTxt);
            holder.quoteTxt.setText(homeRP.getActive_quote());
            if (homeRP.getHeadline().isEmpty()){
                holder.headlineTxt.setVisibility(View.GONE);
            }
            holder.titleTxt.setText(homeRP.getCourse_title());
            holder.headlineTxt.setText(homeRP.getHeadline());
            String progressTxt = String.valueOf(homeRP.getCompleted_lessons())
                    + " of " + String.valueOf(homeRP.getTotal_lessons()) + " lessons done";
            holder.progressTxt.setText(progressTxt);
            if (homeRP.getTotal_lessons() == 0){
                homeRP.setTotal_lessons(1);   //Todo: Inform rehan about this to make sure total lessons should not be zero
                homeRP.setCompleted_lessons(1);
            }
            holder.progressBar.setProgress((int)(100*homeRP.getCompleted_lessons()/homeRP.getTotal_lessons()), true);
        }

        else if (gHolder instanceof UnitViewHolder){
            UnitViewHolder holder = (UnitViewHolder) gHolder;
            ShortUnit unit = homeRP.getUnits().get(position);
            holder.titleTxt.setText(unit.getUnit_title());

            if (unit.isIs_locked()){
                holder.lockImg.setVisibility(View.VISIBLE);
            } else if (unit.getCompleted_lessons() > 0){
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar
                        .setProgress((int)(unit.getCompleted_lessons()/unit.getTotal_lessons()),
                                true);

                String progressTxt = String.valueOf(unit.getCompleted_lessons())
                        + " of " + String.valueOf(unit.getTotal_lessons()) + " lessons";
                holder.progressTxt.setText(progressTxt);
            }

            if (unit.getCompleted_lessons() <= 0){
                String paid = "Free";
                if (unit.isIs_paid())
                    paid = "Paid";
                String totalLessons = String.valueOf(unit.getTotal_lessons())+ " Lessons";
                String connector = "              ";
                holder.progressTxt.setText(paid+connector+totalLessons);
            }

            if (unit.getTags() == null || unit.getTags().size() == 0){
                holder.headlineTxt.setVisibility(View.GONE);
            } else {
                String headline = "";
                for (int i = 0; i < unit.getTags().size(); i++){
                    headline = headline + unit.getTags().get(i);
                    if (i == unit.getTags().size()-1)
                        break;
                    headline = headline+", ";
                }
                holder.headlineTxt.setText(headline);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Todo: Implement Unit Activity
                    Toast.makeText(activity, "Item clicked!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return homeRP.getUnits().size() + 1;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt;
        TextView quoteTxt;
        TextView headlineTxt;
        TextView titleTxt;
        TextView progressTxt;
        LinearProgressIndicator progressBar;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            quoteTxt = itemView.findViewById(R.id.quoteTxt);
            headlineTxt = itemView.findViewById(R.id.headlineTxt);
            titleTxt = itemView.findViewById(R.id.courseTitle);
            progressTxt = itemView.findViewById(R.id.lessonProgressTxt);
            progressBar = itemView.findViewById(R.id.lessonProgressBar);
        }
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder{
        TextView headlineTxt;
        TextView titleTxt;
        TextView progressTxt;
        CircularProgressIndicator progressBar;
        ImageView lockImg;


        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);

            headlineTxt = itemView.findViewById(R.id.headlineTxt);
            titleTxt = itemView.findViewById(R.id.title);
            progressTxt = itemView.findViewById(R.id.progressTxt);
            progressBar = itemView.findViewById(R.id.progressBar);
            lockImg = itemView.findViewById(R.id.lockImg);
        }
    }
}
