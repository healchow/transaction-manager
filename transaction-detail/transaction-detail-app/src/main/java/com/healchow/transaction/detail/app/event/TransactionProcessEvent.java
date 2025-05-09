package com.healchow.transaction.detail.app.event;

import com.healchow.transaction.detail.domain.TransactionDetail;
import org.springframework.context.ApplicationEvent;

/**
 * Event for transaction processing asynchronously
 */
public class TransactionProcessEvent extends ApplicationEvent {

    /**
     * Transaction detail
     */
    private final TransactionDetail detail;

    public TransactionProcessEvent(Object source, TransactionDetail detail) {
        super(source);
        this.detail = detail;
    }

    public TransactionDetail getTransactionDetail() {
        return detail;
    }

}
