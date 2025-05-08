package com.healchow.transaction.detail.web.handler;

import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleMessageHandler {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code) {
        return this.getMessage(code, new Object[]{});
    }

    public String getMessage(String code, String defaultMessage) {
        return this.getMessage(code, null, defaultMessage);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return this.getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Locale locale) {
        return this.getMessage(code, null, "", locale);
    }

    public String getMessage(String code, Object[] args) {
        return this.getMessage(code, args, "");
    }

    private Object[] transArgs(Object[] args) {
        if (args == null) {
            return new Object[0];
        }

        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Number) {
                result[i] = String.valueOf(args[i]);
            } else {
                result[i] = args[i];
            }
        }
        return result;
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return this.getMessage(code, args, "", locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        Object[] objects = transArgs(args);
        return messageSource.getMessage(code, objects, defaultMessage, locale);
    }
}
