package com.healchow.transaction.detail.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healchow.transaction.detail.domain.exception.ServiceException;
import com.healchow.transaction.detail.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @Resource
    private LocaleMessageHandler localeMessage;

    @Resource
    private ObjectMapper objectMapper;

    @ExceptionHandler(ConstraintViolationException.class)
    public Response<String> handleConstraintViolationException(HttpServletRequest request,
            ConstraintViolationException e) {
        logError(e, request);

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder stringBuilder = new StringBuilder(64);
        stringBuilder.append("Invalid params: ");
        for (ConstraintViolation<?> violation : violations) {
            stringBuilder.append(violation.getMessage()).append(", ");
        }

        return Response.fail(stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleMethodArgumentNotValidException(HttpServletRequest request,
            MethodArgumentNotValidException e) {
        logError(e, request);

        StringBuilder builder = new StringBuilder();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(
                error -> builder.append("[").append(error.getField()).append("] ")
                        .append(error.getDefaultMessage())
                        .append(System.lineSeparator()));

        result.getGlobalErrors().forEach(
                error -> builder.append(error.getDefaultMessage()).append(System.lineSeparator()));

        return Response.fail(HttpStatus.BAD_REQUEST.value(), builder.toString());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response<String> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        logError(e, request);

        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public Response<String> handleBindExceptionHandler(HttpServletRequest request, BindException e) {
        logError(e, request);

        StringBuilder builder = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(
                error -> builder.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage()).append(System.lineSeparator()));
        return Response.fail(builder.toString());
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public Response<String> handleHttpMessageConversionExceptionHandler(HttpServletRequest request,
            HttpMessageConversionException e) {
        logError(e, request);
        return Response.fail("HTTP message convert exception, please check params");
    }

    /**
     * Handle the ServiceException
     */
    @ExceptionHandler(value = {ServiceException.class})
    public final ResponseEntity<?> handleServiceException(ServiceException e, HttpServletRequest request) {
        logError(e, request);
        WebExceptionWrapper webExceptionWrapper = new WebExceptionWrapper(e);
        return new ResponseEntity<>(webExceptionWrapper.toResponse(), null, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public Response<String> handle(HttpServletRequest request, Exception e) {
        logError(e, request);

        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error happened in the service, detail: " + e.getMessage());
    }

    @Data
    @ToString
    public class WebExceptionWrapper {

        private Exception rawValue;

        private String i18n;

        private int code;

        private Object[] args;

        public WebExceptionWrapper(ServiceException rawValue) {
            this.rawValue = rawValue;
            this.args = rawValue.getArgs();
            String message = localeMessage.getMessage(rawValue.getI18n(), args);
            i18n = StringUtils.isBlank(message) ? rawValue.getMessage() : message;
            this.code = rawValue.getCode();
        }

        public Response<String> toResponse() {
            return Response.fail(code, rawValue.getLocalizedMessage());
        }
    }

    public void logError(Exception ex, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(8);
        map.put("message", ex.getMessage());
        map.put("from", request.getRemoteAddr());
        String queryString = request.getQueryString();
        map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());
        try {
            log.debug(String.format("Original request: %s, exception: ", objectMapper.writeValueAsString(map)), ex);
        } catch (JsonProcessingException ignored) {
        }
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
