package com.quaser.edtechapp.rest.response;

import java.util.ArrayList;
import java.util.HashMap;

public class TagsRP {
    ArrayList<String> tags;
    HashMap<String, ArrayList<String>> related;

    public ArrayList<String> getTags() {
        return tags;
    }

    public HashMap<String, ArrayList<String>> getRelated() {
        return related;
    }

    public TagsRP() {
    }
}
