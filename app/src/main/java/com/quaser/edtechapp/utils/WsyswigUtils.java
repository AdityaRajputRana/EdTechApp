package com.quaser.edtechapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.wsywig.Editor;

import java.util.HashMap;
import java.util.Map;

public class WsyswigUtils {

    public static Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Poppins_SemiBold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Poppins_ExtraBold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Poppins_SemiBoldItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Poppins-ExtraBoldItalic.ttf");
        return typefaceMap;
    }

    public static Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Poppins-Regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Poppins-Medium.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Poppins-Italic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Poppins-MediumItalic.ttf");
        return typefaceMap;
    }

    @SuppressLint("ResourceType")
    public static void renderBody(Editor editor, String html, Context context) {
        Map<Integer, String> headingTypeface = getHeadingTypeface();
        Map<Integer, String> contentTypeface = getContentface();
        editor.setHeadingTypeface(headingTypeface);
        editor.setContentTypeface(contentTypeface);
        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);
        editor.setNormalTextSize(20);
        editor.setEditorTextColor(context.getResources().getString(R.color.color_primary_txt));

        editor.clearAllContents();
        editor.render(html);
    }
}
