package com.quaser.edtechapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static String getFullFileName(Uri uri, Context context){
        String fileName = "File";
        Cursor cursor =  context.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                fileName = displayName;
            }
        } finally {
            cursor.close();
        }
        return fileName;
    }

    public static String getExtension(Uri uri, Context context){
        String filePath = getFullFileName(uri, context);
        String extension =  filePath.substring(filePath.lastIndexOf(".") + 1);
        if (extension == null || extension.isEmpty()){
            extension = "nid";
        }
        return extension;
    }

    public static String getEncodedFile(Uri selectedFileUri, Context context) {
        Uri uri = selectedFileUri;
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            byte[] bytesArray = new byte[is.available()];
            is.read(bytesArray);
            return android.util.Base64.encodeToString(bytesArray, Base64.NO_WRAP);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEncodedImage(Bitmap bitmap, Context context){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return encodedImage;
    }

    public static String getEncodedImage(Uri selectedFileUri, Context context) {
        Uri uri = selectedFileUri;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String ext = FileUtils.getExtension(uri, context);
            if (ext.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            } else if (ext.equals("webp")) {
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
            } else
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            return encodedImage;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
