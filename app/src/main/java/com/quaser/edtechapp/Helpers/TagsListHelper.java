package com.quaser.edtechapp.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;
import com.quaser.edtechapp.R;

import java.util.ArrayList;
import java.util.List;

public class TagsListHelper {


    ChipsInput chipsInput;
    ArrayList<String> selectedTags;
    ArrayList<String> totalTags;

    public void enableEditing(){
        chipsInput.setEnabled(true);
    }

    public void disableEditing(){
        chipsInput.setEnabled(false);
    }

    public TagsListHelper(Context context, View rootView, ArrayList<String> tags){
        if (tags == null){
            tags = new ArrayList<String>();
        }
        totalTags = tags;
        selectedTags = new ArrayList<String>();

        chipsInput = rootView.findViewById(R.id.chipsInput);
        List<Chip> inputChips = new ArrayList<>();
        for (String tag: tags){
            Chip chip = new Chip(tag, "");
            inputChips.add(chip);
        }
        chipsInput.setFilterableList(inputChips);

        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                selectedTags.add(chip.getLabel());
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
              selectedTags.remove(chip.getLabel());
            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text changed
            }
        });

        chipsInput.requestFocus();
    }

    public ArrayList<String> getSelectedTags(){
        return selectedTags;
    }
}
