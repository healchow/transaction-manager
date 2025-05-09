package com.healchow.transaction.detail.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healchow.transaction.detail.domain.valueobj.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Own user ID")
    private String ownUserId;

    @NotEmpty(message = "cannot be empty")
    @Schema(description = "Own account")
    private String ownAccount;

    @Schema(description = "Counterparty user ID")
    private String counterpartyUserId;

    @Schema(description = "Counterparty account")
    private String counterpartyAccount;

    @Schema(description = "Transaction remarks")
    private String remarks;

    @Schema(description = "Device metadata")
    private String deviceInfo;

    @Schema(description = "Timestamp of transaction occurrence")
    private Long timestamp = System.currentTimeMillis();

    /**
     * Check if the transaction type is valid.
     *
     * @return true valid, false invalid
     */
    @JsonIgnore
    @AssertTrue(message = "The TRANSFER_IN requests are not allowed")
    public boolean isTypeValid() {
        return TransactionType.TRANSFER_IN != TransactionType.ofName(type);
    }

    /**
     * Check if the counterparty is valid.
     *
     * @return true valid, false invalid
     */
    @JsonIgnore
    @AssertTrue(message = "If the type is TRANSFER_OUT, the counterparty user and account are required")
    public boolean isCounterpartyValid() {
        TransactionType requestType = TransactionType.ofName(type);
        if (requestType == TransactionType.TRANSFER_IN || requestType == TransactionType.TRANSFER_OUT) {
            return StringUtils.isNotEmpty(counterpartyUserId) && StringUtils.isNotEmpty(counterpartyAccount);
        }
        return true;
    }
}
