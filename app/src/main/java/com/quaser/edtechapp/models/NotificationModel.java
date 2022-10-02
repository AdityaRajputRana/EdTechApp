package com.quaser.edtechapp.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NotificationModel {

        String title;
        String circularImageUrl;
        String image;
        String description;
        String clickableUri;

        public String getClickableUri(){
            return clickableUri;
        }

        public void setClickableUri(String uri){
            this.circularImageUrl = uri;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCircularImageUrl() {
            return circularImageUrl;
        }

        public void setCircularImageUrl(String circularImageUrl) {
            this.circularImageUrl = circularImageUrl;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTime() {
            if (created_at != null && !created_at.isEmpty()){
                try {
                    String format = "YYYY-MM-ddTkk:mm:ss.SSSZ";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    Date date = sdf.parse(created_at);
                    Timestamp timestamp = new Timestamp(date.getTime());
                    return timestamp.getTime();
                } catch (Exception e){
                    e.printStackTrace();
                    return timeOfNotification;
                }

            }
            return timeOfNotification;
        }

        public void setTime(long time) {
            this.timeOfNotification = time;
        }

        public HashMap<String, Object> getExtras() {
            return extras;
        }

        public void setExtras(HashMap<String, Object> extras) {
            this.extras = extras;
        }

        public NotificationModel() {
        }

        public NotificationModel(String title, String icon, String image, String message, String expiresIn, String id,long time, HashMap<String, Object> extras) {
            this.title = title;
            this.circularImageUrl = icon;
            this.image = image;
            this.description = message;
            this.expiresIn = expiresIn;
            this.id = id;
            this.timeOfNotification = time;
            this.extras = extras;
        }

        String expiresIn;

        String id;
        long timeOfNotification;
        String created_at;
        HashMap<String, Object> extras;
    }

