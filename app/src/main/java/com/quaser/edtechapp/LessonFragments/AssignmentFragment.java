package com.quaser.edtechapp.LessonFragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.quaser.edtechapp.utils.Method;


public class AssignmentFragment extends Fragment {

    private String unitId;
    private ShortLesson shortLesson;
    private LessonListener listener;

    private TextView head;
    private TextView body;
    private TextView sampleDownload;
    private TextView uploadTxt;
    private TextView statusTxt;
    private TextView placeholderTxt;
    private ProgressBar progressBar;
    private MaterialButton continueBtn;

    private AssignmentRP assignment;



    public AssignmentFragment() {
        // Required empty public constructor
    }

    public AssignmentFragment(String unitId, ShortLesson shortLesson, LessonListener listener){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        findViews(view);
        setVisibilities();
        setUpTitles();
        fetchAssignment();
        return view;
    }

    private void fetchAssignment() {
        APIMethods.getLesson(shortLesson.getId(),
                AssignmentRP.class, new APIResponseListener<AssignmentRP>() {
                    @Override
                    public void success(AssignmentRP response) {
                        progressBar.setVisibility(View.GONE);
                        assignment = response;
                        showFullLesson();
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        Method.showFailedAlert(getActivity(), "Failed: "
                                + code+" - " + message);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void showFullLesson() {
        if (assignment.getTitle() != null
        && !assignment.getTitle().isEmpty())
            head.setText(assignment.getTitle());

        if (assignment.getBody() != null
        && !assignment.getBody().isEmpty()){
            body.setText(assignment.getBody());
        }

        if (assignment.getSample_url() != null
        && !assignment.getSample_url().isEmpty()){
            sampleDownload.setVisibility(View.VISIBLE);
            sampleDownload.setOnClickListener(view -> downloadSample());
        }

        if (assignment.getPlaceholder() != null
        && assignment.getPlaceholder().isEmpty()){
            placeholderTxt.setText(assignment.getPlaceholder());
        }

        if (assignment.getSubmitted_url() == null
        || assignment.getSubmitted_url().isEmpty()){
            //Todo handle this by letting ppl download their prev asg,
            //and let variable which handle selected assignment not null
        } else {
            uploadTxt.setOnClickListener(view -> selectAssignment());
        }

        if (assignment.getStatus() != null
        && assignment.getStatus().isEmpty())
            statusTxt.setText(assignment.getStatus());

    }

    private static final int PICK_PDF_FILE = 2;

    private void selectAssignment() {
        if (selectedFileUri == null) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");

            startActivityForResult(intent, PICK_PDF_FILE);
        } else {
            selectedFileUri = null;
            uploadTxt.setText("File Removed\n\nClick here to select PDF File.");
            continueBtn.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == PICK_PDF_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                continueBtn.setEnabled(true);
                uri = resultData.getData();
                selectedFileUri = uri;
                showFileDetails(uri);
//                todo uploadFile();
            }
        }
    }

    private Uri selectedFileUri;

    private void showFileDetails(Uri uri) {
        Cursor cursor = getActivity().getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                uploadTxt.setText(displayName + " selected\n\nClick here to remove selection");
            }
        } finally {
            cursor.close();
        }
    }

    private void downloadSample() {
        Intent viewPdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assignment.getSample_url()));
        startActivity(viewPdfIntent);
    }

    private void setVisibilities() {
        sampleDownload.setVisibility(View.GONE);
    }

    private void setUpTitles() {
        head.setText(shortLesson.getName());
        body.setText(shortLesson.getShort_description());

        continueBtn.setOnClickListener(view -> submit());
        continueBtn.setEnabled(false);
    }

    private void submit() {
    }

    private void findViews(View view) {
        head = view.findViewById(R.id.head);
        body = view.findViewById(R.id.body);
        sampleDownload = view.findViewById(R.id.sampleDownload);
        uploadTxt = view.findViewById(R.id.uploadTxt);
        statusTxt = view.findViewById(R.id.statusTxt);
        placeholderTxt = view.findViewById(R.id.placeHolderTxt);
        progressBar = view.findViewById(R.id.progressBar);
        continueBtn = view.findViewById(R.id.continueBtn);
    }
}