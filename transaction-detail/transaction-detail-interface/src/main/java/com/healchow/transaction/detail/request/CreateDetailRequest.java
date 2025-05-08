package com.healchow.transaction.detail.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * Create request
 */
@Data
@RequiredArgsConstructor
@Schema(title = "Create transaction detail request")
public class CreateDetailRequest {

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Transaction type")
    private String type;

    @NotNull(message = "cannot be null")
    @Schema(description = "Transaction amount")
    private BigDecimal amount;

    @Schema(description = "Timestamp of transaction occurrence")
    private Long timestamp = System.currentTimeMillis();

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Counterparty account")
    private String counterparty;

    @Schema(description = "Transaction remarks")
    private String remarks;

    @Schema(description = "Device metadata")
    private String deviceInfo;
}
