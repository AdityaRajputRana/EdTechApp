package com.quaser.edtechapp.rest.requests;

import com.quaser.edtechapp.Auth.AuthUtils;

public class UploadFileReq {
    String user_id = AuthUtils.getUserId();
    String ext;
    String file;
    String content_type;

    public UploadFileReq(String file) {
        this.file = file;
    }
    public UploadFileReq(String file, String ext){
        this.ext = ext;
        this.file = file;
        this.content_type = "image";
    }

}
