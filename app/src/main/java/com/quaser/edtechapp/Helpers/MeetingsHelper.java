package com.quaser.edtechapp.Helpers;

import android.content.Context;

import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.utils.Method;

import us.zoom.sdk.InviteOptions;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingViewsOptions;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class MeetingsHelper {
    Context context;
    ZoomSDK sdk;

    public MeetingsHelper(Context context) {
        this.context = context;
    }

    public  interface Listener{
        void hideProgress();
    }

    public void initialiseMeeting(
            String appKey, String appSecret, String mid, String pw, Listener pLis
    ){
        sdk = ZoomSDK.getInstance();
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = appKey;
        params.appSecret = appSecret;
        params.domain = "zoom.us";
        params.enableLog = true;
        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
                pLis.hideProgress();
                if (errorCode == ZoomError.ZOOM_ERROR_SUCCESS){
                    startMeeting(mid, pw);
                } else {
                    Method.showFailedAlert(context, "Cannot launch meeting. " + errorCode);
                }
            }
            @Override
            public void onZoomAuthIdentityExpired() { }
        };
        sdk.initialize(context, listener, params);
    }

    private void startMeeting(String mid, String pw) {
        MeetingService meetingService = sdk.getMeetingService();
        JoinMeetingOptions opts = new JoinMeetingOptions();


//        opts.no_driving_mode = true;
//        opts.no_invite = true;
//        opts.no_meeting_end_message = true;
//        opts.no_titlebar = true;
//        opts.no_bottom_toolbar = false;
//        opts.no_dial_in_via_phone = true;
//        opts.no_dial_out_to_phone = true;
//        opts.no_disconnect_audio = true;
//        opts.no_share = true;
//        opts.invite_options = InviteOptions.INVITE_VIA_EMAIL + InviteOptions.INVITE_VIA_SMS;
//        opts.no_audio = false;
//        opts.no_video = false;
//        opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE;
//        opts.no_meeting_error_message = true;

        opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_PARTICIPANTS;
        JoinMeetingParams params = new JoinMeetingParams();

        params.displayName = AuthUtils.getPrimName();
        params.meetingNo = mid;
        params.password = pw;

        int response = meetingService.joinMeetingWithParams(context, params, opts);
    }
}
