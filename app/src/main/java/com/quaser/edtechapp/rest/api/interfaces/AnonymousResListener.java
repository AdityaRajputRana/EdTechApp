package com.quaser.edtechapp.rest.api.interfaces;

import com.quaser.edtechapp.rest.response.AnonymousRP;

public interface AnonymousResListener {
    void success(AnonymousRP response);
    void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable);
}
