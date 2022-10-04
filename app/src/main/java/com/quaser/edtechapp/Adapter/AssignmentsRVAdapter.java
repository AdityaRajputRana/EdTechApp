package com.quaser.edtechapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.rest.response.AssignmentListRP;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.squareup.picasso.Picasso;

public class AssignmentsRVAdapter extends RecyclerView.Adapter<AssignmentsRVAdapter.AssignmentHolder> {
    AssignmentListRP assignmentListRP;
    Context context;

    public AssignmentsRVAdapter(AssignmentListRP assignmentListRP, Context context) {
        this.assignmentListRP = assignmentListRP;
        this.context = context;
    }


    @NonNull
    @Override
    public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_assignments
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHolder holder, int position) {
        AssignmentRP assignmentRP = assignmentListRP.getAssignments().get(position);
        if (assignmentRP.getTitle() != null
                && !assignmentRP.getTitle().isEmpty())
            holder.titleTxt.setText(assignmentRP.getTitle());
        else
            holder.titleTxt.setText(assignmentRP.getLesson_id());

        if (assignmentRP.getUnit_title() != null
                && !assignmentRP.getUnit_title().isEmpty())
            holder.bodyTxt.setText(assignmentRP.getUnit_title() + " Unit");
        else
            holder.bodyTxt.setText("Unit " + assignmentRP.getUnit_id());

        String status = assignmentRP.getStatus();
        int drawableRes = -1;
        if (status.equals("Submitted"))
            drawableRes = R.drawable.ic_baseline_assignment_24;
        else if (status.equals("Accepted"))
            drawableRes = R.drawable.ic_baseline_assignment_accepted;
        else if (status.equals("Rejected"))
            drawableRes = R.drawable.ic_baseline_assignment_rejected;

        holder.statusImg.setImageDrawable(context.getDrawable(drawableRes));

        holder.statusTxt.setText(status);
        holder.actionTxt.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        if (assignmentListRP != null && assignmentListRP.getAssignments() != null){
            return assignmentListRP.getAssignments().size();
        }
        return 0;
    }

    public class AssignmentHolder extends RecyclerView.ViewHolder{

        TextView titleTxt;
        TextView bodyTxt;
        TextView statusTxt;
        ImageView statusImg;
        TextView actionTxt;

        public AssignmentHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.title);
            bodyTxt = itemView.findViewById(R.id.body);
            statusTxt = itemView.findViewById(R.id.status);
            actionTxt = itemView.findViewById(R.id.actionTxt);
            statusImg = itemView.findViewById(R.id.statusImg);
        }
    }

}
