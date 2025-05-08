package com.healchow.transaction.detail.domain.exception;

import lombok.Getter;

/**
 * Enum of error code and message
 */
@Getter
public enum ErrorCodeMessage {

    // ------------------------------------------------------------------------------------------------------------
    //                                              Service Error Codes
    //                                              Format: service.xx.xx...
    // ------------------------------------------------------------------------------------------------------------
    DEFAULT_ERROR(0, "Service internal error", "service.internal.error"),
    USER_NOT_EXISTS(1, "User is not exists", "service.user.not-exists"),
    USER_NOT_LOGGED_IN(2, "User is not logged in", "service.user.not-logged-in"),

    // ------------------------------------------------------------------------------------------------------------
    //                                              Other Error Codes
    // ------------------------------------------------------------------------------------------------------------

    ;

    /**
     * Error code
     */
    private final int code;

    /**
     * Error description
     */
    private final String description;

    /**
     * Internationalized error messages
     */
    private final String i18n;

    ErrorCodeMessage(int code, String description, String i18n) {
        this.code = code;
        this.description = description;
        this.i18n = i18n;
    }
}