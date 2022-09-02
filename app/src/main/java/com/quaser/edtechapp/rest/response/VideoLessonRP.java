package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.VideoQualityOption;

import java.util.ArrayList;

public class VideoLessonRP {

    String lesson_id;
    String title;
    String short_description;
    String type;
    boolean is_complete;
    boolean is_locked;

    public String getVideo() {
        return video_url;
    }

    //    ArrayList<VideoQualityOption> video;
    String video_url;

    public void setVideo_url(String video) {
        this.video_url = video;
    }

    String thumbnail_url;
    int total_time;
    int start_at;

    public String getLesson_id() {
        return lesson_id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getType() {
        return type;
    }

    public boolean isIs_complete() {
        return is_complete;
    }

    public boolean isIs_locked() {
        return is_locked;
    }


    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public int getTotal_time() {
        return total_time;
    }

    public int getStart_at() {
        return start_at;
    }

    public VideoLessonRP() {
    }
}
