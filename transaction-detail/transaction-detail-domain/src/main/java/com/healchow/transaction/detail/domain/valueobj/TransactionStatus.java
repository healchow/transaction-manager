package com.healchow.transaction.detail.domain.valueobj;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Status of transaction
 */
@Getter
public enum TransactionStatus {

    SUCCESS(1),

    FAILED(2),

    PENDING(3),

    PROCESSING(4),

    REVOKED(5),

    ;

    /**
     * Enum code to save
     */
    private final int code;

    TransactionStatus(int code) {
        this.code = code;
    }

    private static final Map<String, TransactionStatus> NAME_TYPE_MAP = Arrays.stream(TransactionStatus.values())
            .collect(Collectors.toMap(TransactionStatus::toString, status -> status));

    private static final Map<Integer, TransactionStatus> CODE_TYPE_MAP = Arrays.stream(TransactionStatus.values())
            .collect(Collectors.toMap(TransactionStatus::getCode, type -> type));

    /**
     * Get the instance by status code
     *
     * @param code status code
     * @return enum instance
     */
    public static TransactionStatus ofCode(Integer code) {
        return CODE_TYPE_MAP.getOrDefault(code, null);
    }

    /**
     * Get the instance by status string
     *
     * @param status status string
     * @return enum instance
     */
    public static TransactionStatus ofName(String status) {
        if (status != null) {
            status = status.toUpperCase(Locale.ROOT);
        }
        return NAME_TYPE_MAP.getOrDefault(status, null);
    }

}
