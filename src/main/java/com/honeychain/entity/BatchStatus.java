package com.honeychain.entity;

/**
 * Ordered lifecycle of a honey batch. Order matters: it defines which
 * transitions are legal (see BatchService).
 */
public enum BatchStatus {
    CREATED,
    HARVESTED,
    QUALITY_CHECKED,
    PACKAGED
}
