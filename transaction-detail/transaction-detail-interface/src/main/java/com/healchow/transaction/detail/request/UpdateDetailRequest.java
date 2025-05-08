package com.healchow.transaction.detail.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * Update request
 */
@Data
@RequiredArgsConstructor
@Schema(title = "Update transaction detail request")
public class UpdateDetailRequest {

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Transaction ID")
    private String tid;

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Transaction type")
    private String type;

    @NotNull(message = "cannot be null")
    @Schema(description = "Transaction amount")
    private BigDecimal amount;

    @Schema(description = "Transaction remarks")
    private String remarks;

    @Schema(description = "Device metadata")
    private String deviceInfo;
}
