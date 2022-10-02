package com.quaser.edtechapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.NotificationModel;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.quaser.edtechapp.utils.Transformations.RoundedCornerTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class NotificationsRVAdapter extends RecyclerView.Adapter<NotificationsRVAdapter.MyViewHolder> {

private ArrayList<NotificationModel> notifications;
private Date date;
private String userName;
private Context context;

public NotificationsRVAdapter(ArrayList<NotificationModel> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
        date = new Date();
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null &&
        !FirebaseAuth.getInstance().getCurrentUser().getDisplayName().isEmpty()) {
        userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        } else {
        userName = "Anonymous User";
        }
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String no = new Gson().toJson(notifications.get(position));

        NotificationModel mNotif = new Gson().fromJson(no, NotificationModel.class);

        if (mNotif.getTitle() != null && !mNotif.getTitle().isEmpty()){
        mNotif.setTitle(mNotif.getTitle().replace("{user_name}", userName));
        holder.titleTxt.setText(mNotif.getTitle());
        } else {
        holder.titleTxt.setVisibility(View.GONE);
        }

        if (mNotif.getDescription() != null && !mNotif.getDescription().isEmpty()){
        mNotif.setDescription(mNotif.getDescription().replace("{user_name}", userName));
        holder.messageTxt.setText(mNotif.getDescription());
        } else {
        holder.messageTxt.setVisibility(View.GONE);
        }

        if (mNotif.getClickableUri() != null
        && !mNotif.getClickableUri().isEmpty()){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNotif.getClickableUri()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                context.startActivity(intent);
                        }
                });
        }

        if (mNotif.getCircularImageUrl() != null && !mNotif.getCircularImageUrl().isEmpty()){
                Picasso.get()
                .load(mNotif.getCircularImageUrl())
                .transform(new CircleTransform())
                .into(holder.icon);
        } else {
                holder.icon.setVisibility(View.GONE);
        }

        if (mNotif.getImage() != null && !mNotif.getImage().isEmpty()){
        Picasso.get()
        .load(mNotif.getImage())
        .transform(new RoundedCornerTransformation(25, 20))
        .into(holder.imageView);
        } else {
        holder.imageView.setVisibility(View.GONE);
        }

        if (mNotif.getTitle() != null && mNotif.getTime() != 0){
        Date createdAt = new Date(mNotif.getTime());

        long difference = date.getTime() -  createdAt.getTime();

        int seconds = (int) (difference/1000);
        int minutes = (int) (seconds/60);
        int hours = (int) (minutes/60);

        int days = (int) (hours/24);
        int months = (int) (days/30.5);
        int years = (int) (months/12);

        if (seconds<60){
        holder.timeTxt.setText(String.valueOf(seconds) + " sec");
        } else if (minutes<60){
        holder.timeTxt.setText(String.valueOf(minutes) + " min");
        } else if (hours<24){
        holder.timeTxt.setText(String.valueOf(hours) + "h");
        } else if (days<31){
        holder.timeTxt.setText(String.valueOf(days) + "d");
        } else if (months<12){
        holder.timeTxt.setText(String.valueOf(months) + "m" );
        } else {
        holder.timeTxt.setText(String.valueOf(years) + "y");
        }


        }
        }

@Override
public int getItemCount() {
        return notifications.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView titleTxt;
    TextView messageTxt;
    TextView timeTxt;
    ImageView icon;
    ImageView imageView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        timeTxt = itemView.findViewById(R.id.timeTxt);
        titleTxt = itemView.findViewById(R.id.titleTxt);
        messageTxt = itemView.findViewById(R.id.messageTxt);
        icon = itemView.findViewById(R.id.icon);
        imageView = itemView.findViewById(R.id.image);
    }
}
}