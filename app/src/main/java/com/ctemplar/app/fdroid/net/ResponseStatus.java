package com.ctemplar.app.fdroid.net;

public enum  ResponseStatus {
    RESPONSE_ERROR,
    RESPONSE_ERROR_AUTH_FAILED,
    RESPONSE_ERROR_USERNAME_EXISTS,
    RESPONSE_ERROR_RECOVER_PASS_FAILED,
    RESPONSE_ERROR_CODE_NOT_MATCH,
    RESPONSE_ERROR_PAID,
    RESPONSE_ERROR_TOO_LARGE,
    RESPONSE_ERROR_TOO_MANY_REQUESTS,
    RESPONSE_COMPLETE,
    RESPONSE_NEXT,
    RESPONSE_WAIT_OTP,
    RESPONSE_NEXT_STEP_USERNAME,
    RESPONSE_NEXT_STEP_EMAIL,
    RESPONSE_NEXT_RECOVER_PASSWORD,
    RESPONSE_NEXT_NEW_PASSWORD,
    RESPONSE_NEXT_MESSAGES,
    RESPONSE_NEXT_MAILBOXES,
    RESPONSE_NEXT_CONTACTS
}
