package com.quaser.edtechapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.AbstractConcatenatedTimeline;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortEvent;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.response.EventRP;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ShortEvent> events;
    ShortLesson shortLesson;
    EventRP eventRP;
    Context context;

    public EventsRVAdapter(ArrayList<ShortEvent> events, ShortLesson shortLesson, Context context) {
        this.events = events;
        this.shortLesson = shortLesson;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (events != null && events.get(position) != null)
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_event_head, parent,false);
            return new HeadViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_event, parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder){
            HeadViewHolder mHolder = (HeadViewHolder) holder;

            //Title Block
            String title = "";
            if (shortLesson.getName() != null
            && !shortLesson.getName().isEmpty()){
                title = shortLesson.getName();
            }

            if (eventRP != null
            && eventRP.getTitle() != null
                    && !eventRP.getTitle().isEmpty()){
                title = eventRP.getTitle();
            }

            if (title.isEmpty())
                mHolder.head.setVisibility(View.GONE);
            else{
                mHolder.head.setText(title);
                mHolder.head.setVisibility(View.VISIBLE);
            }

            //Body Block

            String body = "";
            if (shortLesson.getShort_description() != null
                    && !shortLesson.getShort_description().isEmpty()){
                body = shortLesson.getShort_description();
            }

            if (eventRP != null
                    && eventRP.getDescription() != null
                    && !eventRP.getDescription().isEmpty()){
                body = eventRP.getDescription();
            }

            if (body.isEmpty())
                mHolder.body.setVisibility(View.GONE);
            else{
                mHolder.body.setText(body);
                mHolder.body.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof EventViewHolder){
            ShortEvent event = events.get(position);
            EventViewHolder holderEvent = (EventViewHolder) holder;

            if (event.getTitle() != null && !event.getTitle().isEmpty()){
                holderEvent.head.setText(event.getTitle());
                holderEvent.head.setVisibility(View.VISIBLE);
            } else {
                holderEvent.head.setVisibility(View.GONE);
            }

            if (event.getDescription() != null && !event.getDescription().isEmpty()){
                holderEvent.body.setText(event.getDescription());
                holderEvent.body.setVisibility(View.VISIBLE);
            } else {
                holderEvent.body.setVisibility(View.GONE);
            }

            if (event.getTime() != null &&
                    event.getTime().getDate() != 0){
                holderEvent.dateTxt.setText(String.valueOf(event.getTime().getDate()));
                holderEvent.dateTxt.setVisibility(View.VISIBLE);
            } else {
                holderEvent.dateTxt.setVisibility(View.GONE);
            }

            if (event.getTime() != null &&
                    event.getTime().getMonth() != null &&
                    !event.getTime().getMonth().isEmpty()){
                holderEvent.monthTxt.setText(event.getTime().getMonth());
                holderEvent.monthTxt.setVisibility(View.VISIBLE);
            } else {
                holderEvent.monthTxt.setVisibility(View.GONE);
            }

            if (event.getType() != null && !event.getType().isEmpty()){
                holderEvent.onlineTxt.setText(event.getType());
            } else {
                holderEvent.onlineTxt.setText("Event");
            }
            holderEvent.onlineTxt.setVisibility(View.VISIBLE);

            if (event.getSeats() != null) {

                if (event.getSeats().getTotal() == -1) {
                    holderEvent.seatsTxt.setText("Open to all");
                } else {

                    String seats = "";
                    seats = event.getSeats().getFilled()
                            + "/" + event.getSeats().getTotal() + " Seats";
                    holderEvent.seatsTxt.setText(seats);
                }
                holderEvent.seatsTxt.setVisibility(View.VISIBLE);
            } else {
                holderEvent.seatsTxt.setVisibility(View.GONE);
            }


            if ((event.getIs_paid() == null || !event.getIs_paid())
            && event.getPrice() == 0){
                holderEvent.priceTxt.setText("Price: Free");
            } else {
                holderEvent.priceTxt.setText("Price: ₹" + event.getPrice());
            }
            holderEvent.priceTxt.setVisibility(View.VISIBLE);

            if (event.getIs_locked() == null || !event.getIs_locked()){
                holderEvent.lockLayout.setVisibility(View.GONE);
                holderEvent.itemView.setOnClickListener(view -> {
                    launchEventActivity(event);
                });
            } else {
                holderEvent.lockLayout.setVisibility(View.VISIBLE);
                holderEvent.itemView.setOnClickListener(view -> {
                    Toast.makeText(context, "First Complete Pre-req!", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void launchEventActivity(ShortEvent event) {
        //Todo: launch event activity
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvents(EventRP eventRP){
        this.eventRP = eventRP;
        events.addAll(eventRP.getEvents());
        notifyItemRangeInserted(1, eventRP.getEvents().size());
        notifyItemChanged(0);
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder{
        TextView head;
        TextView body;
        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            body = itemView.findViewById(R.id.body);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView head;
        TextView body;
        TextView dateTxt;
        TextView monthTxt;
        TextView onlineTxt;
        TextView seatsTxt;
        TextView priceTxt;
        LinearLayout lockLayout;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            body = itemView.findViewById(R.id.body);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            monthTxt = itemView.findViewById(R.id.monthTxt);
            onlineTxt = itemView.findViewById(R.id.onlineTxt);
            seatsTxt = itemView.findViewById(R.id.seatsTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            lockLayout = itemView.findViewById(R.id.lockLayout);
        }
    }
}
