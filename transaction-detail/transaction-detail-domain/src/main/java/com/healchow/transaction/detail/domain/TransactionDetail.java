package com.healchow.transaction.detail.domain;

import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resource order info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = -1060957012587016056L;

    /**
     * Transaction ID
     */
    private String tid;

    /**
     * User ID initiating the transaction, associated with user identity in banking system
     */
    private String userId;

    /**
     * Transaction type, {@link TransactionType}
     */
    private Integer type;

    /**
     * Amount involved in transaction
     */
    private BigDecimal amount;

    /**
     * Timestamp of transaction occurrence, precise to the millisecond
     */
    private Long timestamp;

    /**
     * Current transaction status,
     */
    private Integer status;

    /**
     * Counterparty account
     */
    private String counterparty;

    /**
     * Post-transaction account balance
     */
    private BigDecimal accountBalance;

    /**
     * Additional transaction context (e.g. transfer purpose, consumption description)
     */
    private String remarks;

    /**
     * Device metadata (type, IP address) for security monitoring and anomaly detection
     */
    private String deviceInfo;

    /**
     * Create time for record
     */
    private LocalDateTime createTime;

    /**
     * Update time for record
     */
    private LocalDateTime updateTime;

}
