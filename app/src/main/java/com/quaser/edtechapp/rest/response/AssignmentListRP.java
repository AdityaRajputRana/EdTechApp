package com.quaser.edtechapp.rest.response;

import java.util.ArrayList;

public class AssignmentListRP {
    int page;
    int count;
    int pages;
    ArrayList<AssignmentRP> assignments;

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public ArrayList<AssignmentRP> getAssignments() {
        return assignments;
    }

    public AssignmentListRP() {
    }

    public AssignmentListRP(ArrayList<AssignmentRP> assignments) {
        this.assignments = assignments;
        this.page = 1;
        this.pages = 1;
        if (assignments != null)
            this.count = assignments.size();
    }
}
