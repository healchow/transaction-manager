package com.healchow.transaction.detail.domain.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * Exception for service.
 */
@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6756047087037291573L;

    private final Integer code;

    private final String i18n;

    /**
     * Parameters used to fill placeholders in the internationalized error message
     */
    private final Object[] args;

    private final Object data;

    public ServiceException(ErrorCodeMessage codeMessage) {
        this(codeMessage, null, null, null);
    }

    /**
     * This String message will not be appended as args to the internationalized information of ErrorCodeMessage
     */
    public ServiceException(ErrorCodeMessage codeMessage, String message) {
        this(codeMessage, null, message, new Object[]{message});
    }

    public ServiceException(ErrorCodeMessage codeMessage, Throwable cause) {
        this(codeMessage, cause, null, null);
    }

    /**
     * Build default exception information with the specified message
     *
     * @param message Exception description
     * @apiNote Since the default ErrorCodeMessage needs to fill the placeholder for the exception reason, the message needs to be passed as an internationalization parameter
     */
    public ServiceException(String message) {
        this(ErrorCodeMessage.DEFAULT_ERROR, null, message, new Object[]{message});
    }

    /**
     * Build default exception information with the specified exception stack and exception description
     *
     * @param message Exception description
     * @param cause Exception stack
     * @apiNote Since the default ErrorCodeMessage needs to fill the placeholder for the exception reason, the message needs to be passed as an internationalization parameter
     */
    public ServiceException(String message, Throwable cause) {
        this(ErrorCodeMessage.DEFAULT_ERROR, cause, message, new Object[]{message});
    }

    private ServiceException(ErrorCodeMessage codeMessage, Throwable cause, String data, Object[] args) {
        super(data == null ? codeMessage.getDescription() : codeMessage.getDescription() + ", info: " + data, cause);
        this.code = codeMessage.getCode();
        this.i18n = codeMessage.getI18n();
        this.data = data;
        this.args = args;
    }

    /**
     * Construct service exception information through error code and parameters
     *
     * @param codeMessage Error code
     * @param args Parameters in the internationalized description information
     * @return Exception information
     */
    public static ServiceException createWithCodeAndArgs(ErrorCodeMessage codeMessage, Object... args) {
        return new ServiceException(codeMessage, null, null, args);
    }

}