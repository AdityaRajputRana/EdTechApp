package com.quaser.edtechapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TagsRVAdapter extends RecyclerView.Adapter<TagsRVAdapter.TagsViewholder>{
    String selectedId;
    ArrayList<String> tags;
    Context context;
    Listener listener;

    public interface Listener{
        void selectSubCat(String tag);
    }

    public void resetToAll(){
        selectedId = "All";
        listener.selectSubCat(selectedId);
        notifyDataSetChanged();
    }
    public TagsRVAdapter(String selectedId, ArrayList<String> tags, Context context, Listener listener) {
        this.selectedId = selectedId;
        this.tags = tags;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public TagsViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TagsViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TagsRVAdapter.TagsViewholder holder, int index) {

        final int position = index;

        if (tags.get(position).equals(selectedId)){
            holder.itemView.setBackground(context.getDrawable(R.drawable.bg_tag_selected));
            holder.textView.setTextColor(context.getResources().getColor(R.color.color_secondary_txt));
        } else {
            holder.itemView.setBackground(context.getDrawable(R.drawable.bg_tag_disselected));
            holder.textView.setTextColor(context.getResources().getColor(R.color.color_secondary_txt));
        }

        holder.textView.setText(tags.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedId != tags.get(position)) {
                    listener.selectSubCat(tags.get(position));
                    selectedId = tags.get(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class TagsViewholder extends RecyclerView.ViewHolder{

        TextView textView;

        public TagsViewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }
}
