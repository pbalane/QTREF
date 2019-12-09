package com.qtref.model;

/**
 * This is used to maintain the status after each transaction list service execution
 */
public enum StateEnum {
    DRAFT,
    PREPARED,
    CHECKED,
    VERIFIED,
    COMPLTETED,
    UN_ASSIGNED,
    RE_SET// To avoid null pointer exception in Event Manager
}
