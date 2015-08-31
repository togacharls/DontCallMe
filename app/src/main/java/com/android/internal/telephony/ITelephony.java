package com.android.internal.telephony;

/**
 * Created by carlos on 2/06/15.
 */
public interface ITelephony {

    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
