package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.DataRp;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.FileUtils;
import com.quaser.edtechapp.utils.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddQuestionActivity extends AppCompatActivity {

    boolean hasEditorInput = false;
    ArrayList<String> mediaInput;
    String serialisedBody = "";

    private int pageState = 0;

    private LinearLayout headLayout;
    private ConstraintLayout bodyLayout;
    private LinearLayout tagsLayout;

    @Override
    public void onBackPressed() {
        switch (pageState){
            case 1:
                headLayout.setVisibility(View.VISIBLE);
                bodyLayout.setVisibility(View.GONE);
                headEt.requestFocus();
                pageState--;
                break;

            case 2:
                bodyLayout.setVisibility(View.VISIBLE);
                editor.requestFocus();
                tagsLayout.setVisibility(View.GONE);
                continueBtn.setText("NEXT");
                break;

            default:
                super.onBackPressed();
        }
    }

    private void proceed(){
        switch (pageState){
            case 0:
                if (headEt.getText().toString().isEmpty())
                    headEt.setError("Required");
                else {
                    headEt.setError(null);
                    headLayout.setVisibility(View.GONE);
                    bodyLayout.setVisibility(View.VISIBLE);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(headEt.getWindowToken(), 0);

                    if (!hasEditorInput){
                        setUpEditor();
                    }
                    pageState++;
                }
                break;
            case 1:
                String string = editor.getContentAsSerialized();
                if (string.isEmpty()){
                    Toast.makeText(this, "Description is required!", Toast.LENGTH_SHORT).show();
                } else {
                    serialisedBody = string;
                    bodyLayout.setVisibility(View.GONE);
                    tagsLayout.setVisibility(View.VISIBLE);
                    continueBtn.setText("POST");
                }
                break;
        }
    }

    private MaterialButton continueBtn;
    EditText headEt;
    EditText imgEt;
    EditText tagsEt;
    EditText bodyEt;

    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        
        
        findViews();
        setListeners();
        editor =  findViewById(R.id.editor);
    }

        @SuppressLint("ResourceType")
        private void setUpEditor() {
            findViewById(com.github.irshulx.R.id.action_h1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H1);
                }
            });

            findViewById(com.github.irshulx.R.id.action_h2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H2);
                }
            });

            findViewById(com.github.irshulx.R.id.action_h3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H3);
                }
            });


            findViewById(com.github.irshulx.R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.ITALIC);
                }
            });

            findViewById(com.github.irshulx.R.id.action_indent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.INDENT);
                }
            });

            findViewById(com.github.irshulx.R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
                }
            });

            findViewById(com.github.irshulx.R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.OUTDENT);
                }
            });

            findViewById(com.github.irshulx.R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertList(false);
                }
            });

            findViewById(com.github.irshulx.R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertList(true);
                }
            });

            findViewById(com.github.irshulx.R.id.action_hr).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertDivider();
                }
            });

            findViewById(com.github.irshulx.R.id.action_color).setVisibility(View.GONE);


//            findViewById(com.github.irshulx.R.id.action_color).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new ColorPickerPopup.Builder(AddQuestionActivity.this)
//                            .initialColor(Color.RED) // Set initial color
//                            .enableAlpha(true) // Enable alpha slider or not
//                            .okTitle("Choose")
//                            .cancelTitle("Cancel")
//                            .showIndicator(true)
//                            .showValue(true)
//                            .build()
//                            .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
//                                @Override
//                                public void onColorPicked(int color) {
//                                    Toast.makeText(EditorTestActivity.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
//                                    editor.updateTextColor(colorHex(color));
//                                }
//
//                                @Override
//                                public void onColor(int color, boolean fromUser) {
//
//                                }
//                            });
//
//
//                }
//            });

            findViewById(com.github.irshulx.R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.openImagePicker();
                }
            });

            findViewById(com.github.irshulx.R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertLink();
                }
            });

            findViewById(com.github.irshulx.R.id.action_bold).setOnClickListener(view -> {
                editor.updateTextStyle(EditorTextStyle.BOLD);
                Toast.makeText(this, "Bold!", Toast.LENGTH_SHORT).show();
            });


            findViewById(com.github.irshulx.R.id.action_erase).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.clearAllContents();
                }
            });
//            editor.dividerBackground=R.drawable.divider_background_dark;
//            editor.setFontFace(get);
            Map<Integer, String> headingTypeface = getHeadingTypeface();
            Map<Integer, String> contentTypeface = getContentface();
            editor.setHeadingTypeface(headingTypeface);
            editor.setContentTypeface(contentTypeface);
            editor.setDividerLayout(com.github.irshulx.R.layout.tmpl_divider_layout);
            editor.setEditorImageLayout(com.github.irshulx.R.layout.tmpl_image_view);
            editor.setListItemLayout(com.github.irshulx.R.layout.tmpl_list_item);
            editor.setNormalTextSize(20);
            editor.setEditorTextColor(AddQuestionActivity.this.getResources().getString(R.color.color_primary_txt));
            editor.setEditorListener(new EditorListener() {
                @Override
                public void onTextChanged(EditText editText, Editable text) {
                    // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpload(Bitmap image, String uuid) {
                    Toast.makeText(AddQuestionActivity.this, "Uploading image:" + uuid, Toast.LENGTH_LONG).show();
                    APIMethods.uploadForumFile(FileUtils.getEncodedImage(image, AddQuestionActivity.this), "png", new APIResponseListener<DataRp>() {
                        @Override
                        public void success(DataRp response) {
                            Toast.makeText(AddQuestionActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            mediaInput.add(response.getLink());
                            editor.onImageUploadComplete(response.getLink(), uuid);
                        }

                        @Override
                        public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                            Toast.makeText(AddQuestionActivity.this, "Upload image failed!", Toast.LENGTH_SHORT).show();
                            editor.onImageUploadFailed(uuid);
                        }
                    });
                }

                @Override
                public View onRenderMacro(String name, Map<String, Object> props, int index) {
                    View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                    return view;
                }

            });

            findViewById(com.github.irshulx.R.id.action_erase).performClick();


        }

    public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Poppins_SemiBold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Poppins_ExtraBold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Poppins_SemiBoldItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Poppins-ExtraBoldItalic.ttf");
        return typefaceMap;
    }

    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Poppins-Regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Poppins-Medium.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Poppins-Italic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Poppins-MediumItalic.ttf");
        return typefaceMap;
    }

    private void setListeners() {
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });

        headEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    proceed();
                    return true;
                }
                return false;
            }
        });
    }

    private void processData() {
        disable();
        ArrayList<String> tagsList = new ArrayList<>();
        if (!tagsEt.getText().toString().isEmpty()){
            String[] tags = tagsEt.getText().toString().split(",");
            tagsList= new ArrayList<String>(Arrays.asList(tags));
        }

        postQuestion(tagsList);
    }

    private void postQuestion(ArrayList<String> tagsList) {
        APIMethods.postQuestion(headEt.getText().toString(),
                bodyEt.getText().toString(), imgEt.getText().toString(), tagsList,
                this, new APIResponseListener<QuestionRP>() {
                    @Override
                    public void success(QuestionRP response) {
                        Toast.makeText(AddQuestionActivity.this, "Question Added Successfully!", Toast.LENGTH_SHORT).show();
                        launchViewQuestionActivity(response);
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        enable();
                        Method.showFailedAlert(AddQuestionActivity.this, code+"-"+message);
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                editor.insertImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchViewQuestionActivity(QuestionRP response) {
        String questionRP = new Gson().toJson(response);
        Intent intent = new Intent(this, ViewQuestionActivity.class);
        intent.putExtra("hasQuestionAttached", true);
        intent.putExtra("isNewQuestion", true);
        intent.putExtra("question", questionRP);
        startActivity(intent);
        this.finish();
    }

    private void disable(){
        continueBtn.setEnabled(false);
        headEt.setEnabled(false);
        bodyEt.setEnabled(false);
        imgEt.setEnabled(false);
        tagsEt.setEnabled(false);
    }

    private void enable(String message){
        continueBtn.setEnabled(true);
        headEt.setEnabled(true);
        bodyEt.setEnabled(true);
        imgEt.setEnabled(true);
        tagsEt.setEnabled(true);
        if (!message.isEmpty()){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void enable(){
        enable("");
    }

    private boolean areFieldsFilled() {
        boolean areFilled = true;
        if (headEt.getText().toString().isEmpty()){
            headEt.setError("This is required");
            areFilled = false;
        } else {
            headEt.setError(null);
        }

        if (bodyEt.getText().toString().isEmpty()){
            bodyEt.setError("This is required");
            areFilled = false;
        } else {
            bodyEt.setError(null);
        }
        return areFilled;
    }

    private void findViews() {
        continueBtn = findViewById(R.id.continueBtn);

        headLayout = findViewById(R.id.questionTitleLayout);
        bodyLayout = findViewById(R.id.questionBodyLayout);
        tagsLayout = findViewById(R.id.tagsLayout);

        headEt = findViewById(R.id.headEt);
        imgEt = findViewById(R.id.headEt);
        tagsEt = findViewById(R.id.headEt);
        bodyEt = findViewById(R.id.headEt);
    }
}