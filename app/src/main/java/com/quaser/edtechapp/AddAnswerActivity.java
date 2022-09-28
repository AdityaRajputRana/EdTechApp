package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.DataRp;
import com.quaser.edtechapp.utils.FileUtils;
import com.quaser.edtechapp.wsywig.Editor;
import com.quaser.edtechapp.wsywig.EditorListener;
import com.quaser.edtechapp.wsywig.models.EditorTextStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAnswerActivity extends AppCompatActivity {

    Editor editor;
    ProgressBar progressBar;
    MaterialButton continueBtn;
    ArrayList<String> mediaInput = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        findViews();
        setListeners();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Your written answer would be lost.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AddAnswerActivity.super.onBackPressed();
                    }
                })
                .setCancelable(true)
                .show();
    }

    private void setListeners() {
        findViewById(R.id.backBtn).setOnClickListener(view -> onBackPressed());
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor.getContentAsHTML().isEmpty()){
                    Toast.makeText(AddAnswerActivity.this, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    processData();
                }
            }
        });
    }

    private void processData() {
        String answer = editor.getContentAsHTML();
        String questionId = getIntent().getStringExtra("QID");
    }
    
    private void disable(){
        continueBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        editor.setEnabled(false);
    }

    private void enable(){
        progressBar.setVisibility(View.GONE);
        continueBtn.setVisibility(View.VISIBLE);
        editor.setEnabled(true);
    }

    private void findViews() {
        editor = findViewById(R.id.editor);
        progressBar = findViewById(R.id.progressBar);
        continueBtn = findViewById(R.id.continueBtn);
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
//                    new ColorPickerPopup.Builder(AddAnswerActivity.this)
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
        editor.setNormalTextSize(20);
        editor.setEditorTextColor(AddAnswerActivity.this.getResources().getString(R.color.color_primary_txt));
        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(AddAnswerActivity.this, "Uploading image:" + uuid, Toast.LENGTH_LONG).show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        APIMethods.uploadForumFile(FileUtils.getEncodedImage(image, AddAnswerActivity.this), "png", new APIResponseListener<DataRp>() {
                            @Override
                            public void success(DataRp response) {
                                AddAnswerActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddAnswerActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                        mediaInput.add(response.getLink());
                                        editor.onImageUploadComplete(response.getLink(), uuid);
                                    }
                                });
                            }

                            @Override
                            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                                AddAnswerActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddAnswerActivity.this, "Upload image failed!", Toast.LENGTH_SHORT).show();
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
}