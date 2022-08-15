package com.quaser.edtechapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import java.util.ArrayList;

public class ForrumChipRVAdapter extends RecyclerView.Adapter<ForrumChipRVAdapter.MyViewholder> {

    ArrayList<String> tags;

    public ForrumChipRVAdapter(ArrayList<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_question_tags, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int mPos) {
        final int position = mPos;
        holder.textView.setText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
