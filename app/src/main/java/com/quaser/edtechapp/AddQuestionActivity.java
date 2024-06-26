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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//
import com.quaser.edtechapp.wsywig.Editor;
import com.quaser.edtechapp.wsywig.EditorListener;
import com.quaser.edtechapp.wsywig.models.EditorTextStyle;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Helpers.TagsListHelper;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.DataRp;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.rest.response.TagsRP;
import com.quaser.edtechapp.utils.FileUtils;
import com.quaser.edtechapp.utils.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddQuestionActivity extends AppCompatActivity {

    boolean hasEditorInput = false;
    ArrayList<String> mediaInput = new ArrayList<String>();
    String serialisedBody = "";

    private int pageState = 0;

    private LinearLayout headLayout;
    private ConstraintLayout bodyLayout;
    private LinearLayout tagsLayout;

    private ProgressBar tagsProgressBar;

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
                pageState--;
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
                    if (!hasEditorInput){
                        setUpEditor();
                        editor.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
                    loadTags();
                    pageState++;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            case 2:
                continueBtn.setEnabled(false);
                tagsProgressBar.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Posting question", Toast.LENGTH_SHORT).show();
                processData();
                break;
        }
    }

    private TagsListHelper tagsHelper;

    private void loadTags() {
        tagsProgressBar.setVisibility(View.VISIBLE);
        APIMethods.getTags(new APIResponseListener<TagsRP>() {
            @Override
            public void success(TagsRP response) {
                tagsProgressBar.setVisibility(View.GONE);
                tagsHelper = new TagsListHelper(AddQuestionActivity.this, tagsLayout, response.getTags());
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                tagsProgressBar.setVisibility(View.VISIBLE);
                Toast.makeText(AddQuestionActivity.this, "Error loading tags.", Toast.LENGTH_SHORT).show();
                Method.showFailedAlert(AddQuestionActivity.this, code + " - " + message);
            }
        });
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
        headEt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

        @SuppressLint("ResourceType")
        private void setUpEditor() {
            findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H1);
                }
            });

            findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H2);
                }
            });

            findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.H3);
                }
            });


            findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.ITALIC);
                }
            });

            findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.INDENT);
                }
            });

            findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
                }
            });

            findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.updateTextStyle(EditorTextStyle.OUTDENT);
                }
            });

            findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertList(false);
                }
            });

            findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertList(true);
                }
            });

            findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertDivider();
                }
            });

            findViewById(R.id.action_color).setVisibility(View.GONE);


//            findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
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

            findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.openImagePicker();
                }
            });

            findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.insertLink();
                }
            });

            findViewById(R.id.action_bold).setOnClickListener(view -> {
                editor.updateTextStyle(EditorTextStyle.BOLD);
                Toast.makeText(this, "Bold!", Toast.LENGTH_SHORT).show();
            });


            findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
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
            editor.setDividerLayout(R.layout.tmpl_divider_layout);
            editor.setEditorImageLayout(R.layout.tmpl_image_view);
            editor.setListItemLayout(R.layout.tmpl_list_item);
            editor.setNormalTextSize(18);
            editor.setEditorTextColor(AddQuestionActivity.this.getResources().getString(R.color.color_primary_txt));
            editor.setEditorListener(new EditorListener() {
                @Override
                public void onTextChanged(EditText editText, Editable text) {
                    // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpload(Bitmap image, String uuid) {
                    Toast.makeText(AddQuestionActivity.this, "Uploading image:" + uuid, Toast.LENGTH_LONG).show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            APIMethods.uploadForumFile(FileUtils.getEncodedImage(image, AddQuestionActivity.this), "png", new APIResponseListener<DataRp>() {
                                @Override
                                public void success(DataRp response) {
                                    AddQuestionActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddQuestionActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                            mediaInput.add(response.getLink());
                                            editor.onImageUploadComplete(response.getLink(), uuid);
                                        }
                                    });
                                }

                                @Override
                                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                                    AddQuestionActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddQuestionActivity.this, "Upload image failed!", Toast.LENGTH_SHORT).show();
                                            editor.onImageUploadFailed(uuid);
                                        }
                                    });
                                }
                            });
                        }
                    });
                    thread.start();
                }

                @Override
                public View onRenderMacro(String name, Map<String, Object> props, int index) {
                    View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                    return view;
                }

            });

            findViewById(R.id.action_erase).performClick();


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

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void processData() {
        continueBtn.setEnabled(false);
        tagsHelper.disableEditing();
        Log.i("ProcessingData", "processData:");
        if (tagsHelper != null)
            postQuestion(tagsHelper.getSelectedTags());
        else
            postQuestion(null);
    }

    private void postQuestion(ArrayList<String> tagsList) {
        APIMethods.postQuestion(headEt.getText().toString(),
                editor.getContentAsHTML(), editor.getContentAsHTML(), serialisedBody, mediaInput, tagsList,
                this, new APIResponseListener<QuestionRP>() {
                    @Override
                    public void success(QuestionRP response) {
                        Toast.makeText(AddQuestionActivity.this, "Question Added Successfully!", Toast.LENGTH_SHORT).show();
                        launchViewQuestionActivity(response);
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        Method.showFailedAlert(AddQuestionActivity.this, code+"-"+message);
                        continueBtn.setEnabled(true);
                        tagsProgressBar.setVisibility(View.GONE);
                        tagsHelper.enableEditing();
                        continueBtn.setEnabled(true);
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

        tagsProgressBar = findViewById(R.id.tagsProgressBar);
    }
}