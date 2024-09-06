package com.fdmgroup.forex.enums;

/**
 * Denotes the state of the order as follows:
 * ACTIVE: Order is live and open to matches
 * CANCELLED: Order cancelled by the user or the order is closed due to insufficient funds with no execution
 * CLEARED: Order fully executed
 * CLOSED: Order partially executed but closed due to insufficient funds
 * EXPIRED: Order not executed or partially executed upon expiry
 */
public enum OrderStatus {
    ACTIVE, CANCELLED, CLEARED, CLOSED, EXPIRED
}
