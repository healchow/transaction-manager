package com.healchow.transaction.detail.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Transaction detail response
 */
@Data
@Schema(title = "Transaction detail response")
public class DetailResponse {

    @Schema(description = "Transaction ID")
    private String tid;

    @Schema(description = "Transaction type")
    private String type;

    @Schema(description = "Transaction status")
    private String status;

    @Schema(description = "Transaction amount")
    private BigDecimal amount;

    @Schema(description = "Timestamp of transaction occurrence")
    private Long timestamp;

    @Schema(description = "Counterparty account")
    private String counterparty;

    @Schema(description = "Account balance")
    private BigDecimal accountBalance;

    @Schema(description = "Transaction remarks")
    private String remarks;

    @Schema(description = "Device metadata")
    private String deviceInfo;
}
