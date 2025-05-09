package com.healchow.transaction.detail.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Type of transaction
 */
@Getter
@AllArgsConstructor
public enum TransactionType {

    DEPOSIT(1),

    WITHDRAWAL(2),

    TRANSFER_IN(3),

    TRANSFER_OUT(4),

    ;

    /**
     * Enum code to save
     */
    private final int code;

    private static final Map<String, TransactionType> NAME_TYPE_MAP = Arrays.stream(TransactionType.values())
            .collect(Collectors.toMap(TransactionType::toString, type -> type));

    private static final Map<Integer, TransactionType> CODE_TYPE_MAP = Arrays.stream(TransactionType.values())
            .collect(Collectors.toMap(TransactionType::getCode, type -> type));

    /**
     * Get the instance by type code
     *
     * @param code type code
     * @return enum instance
     */
    public static TransactionType ofCode(Integer code) {
        return CODE_TYPE_MAP.getOrDefault(code, null);
    }

    /**
     * Get the instance by type string
     *
     * @param typeStr type string
     * @return enum instance
     */
    public static TransactionType ofName(String typeStr) {
        if (typeStr != null) {
            typeStr = typeStr.toUpperCase(Locale.ROOT);
        }
        return NAME_TYPE_MAP.getOrDefault(typeStr, null);
    }

}