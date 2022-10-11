package com.quaser.edtechapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quaser.edtechapp.EventActivity;
import com.quaser.edtechapp.FrameActivity;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortEvent;
import com.quaser.edtechapp.rest.response.AssignmentListRP;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.quaser.edtechapp.rest.response.EventRP;
import com.quaser.edtechapp.rest.response.SubbedEvents;

public class SubbedEventsRVAdapter extends RecyclerView.Adapter<SubbedEventsRVAdapter.AssignmentHolder> {
    SubbedEvents subbedEvents;
    Context context;

    public SubbedEventsRVAdapter(SubbedEvents subbedEvents, Context context) {
        this.subbedEvents = subbedEvents;
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
        ShortEvent eventRp = subbedEvents.getEvents().get(position);
        if (eventRp.getTitle() != null
                && !eventRp.getTitle().isEmpty())
            holder.titleTxt.setText(eventRp.getTitle());
        else
            holder.titleTxt.setText(eventRp.getEvent_id());

        holder.bodyTxt.setText("");
        if (eventRp.getDescription() != null
                && !eventRp.getDescription().isEmpty())
            holder.bodyTxt.setText(eventRp.getDescription());
        else if (eventRp.getEvent_id() != null){
            holder.bodyTxt.setText("Event Id:"+eventRp.getEvent_id());
        }


        if (eventRp.getTime() != null
                && eventRp.getTime().getDate_full() != null
                && !eventRp.getTime().getDate_full().isEmpty()) {
            if (!holder.bodyTxt.getText().toString().isEmpty())
                holder.bodyTxt.setText(holder.bodyTxt.getText().toString() + "\n");
            holder.bodyTxt.setText(holder.bodyTxt.getText().toString()
                    + "Scheduled on  " + eventRp.getTime().getDate_full());
        }

        String status = eventRp.getType();
        if (status == null || status.isEmpty()){
            status = "";
            if (eventRp.getVenue() != null){
                status = status+eventRp.getVenue();
            }
        }
        int drawableRes = -1;
        if (status.equals("online"))
            drawableRes = R.drawable.ic_baseline_online_event;
        else
            drawableRes = R.drawable.ic_baseline_event_offline;


        holder.statusImg.setImageDrawable(context.getDrawable(drawableRes));

        holder.statusTxt.setText(status);
        holder.actionTxt.setText("View Event Details");
        holder.actionTxt.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("event_id", eventRp.getEvent_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (subbedEvents != null && subbedEvents.getEvents() != null){
            return subbedEvents.getEvents().size();
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
