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

    public ServiceException(ErrorCodeMessage codeMessage) {
        this(codeMessage, null, null);
    }

    public ServiceException(ErrorCodeMessage codeMessage, Throwable cause) {
        this(codeMessage, cause, null);
    }

    private ServiceException(ErrorCodeMessage codeMessage, Throwable cause, Object[] args) {
        super(codeMessage.getDescription(), cause);
        this.code = codeMessage.getCode();
        this.i18n = codeMessage.getI18n();
        this.args = args;
    }

    /**
     * Construct service exception information through error code and parameters
     *
     * @param codeMessage Error code
     * @param args Parameters in the internationalized description information
     * @return Exception information
     */
    public static ServiceException fromErrorCodeAndArgs(ErrorCodeMessage codeMessage, Object... args) {
        return new ServiceException(codeMessage, null, args);
    }

}