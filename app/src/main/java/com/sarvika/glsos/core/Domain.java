package com.sarvika.glsos.core;

/**
 * Life-service domains handled by the GLSOS engine. The {@code key} is a stable
 * identifier used for passing a selection between screens and for localization
 * lookups.
 */
public enum Domain {
    HEALTHCARE("healthcare"),
    FOOD("food"),
    COMMERCE("commerce"),
    EMPLOYMENT("employment"),
    MARRIAGE("marriage"),
    MICROSERVICES("microservices"),
    EDUCATION("education"),
    FINANCE("finance");

    private final String key;

    Domain(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    public static Domain fromKey(String key) {
        if (key != null) {
            for (Domain d : values()) {
                if (d.key.equals(key)) {
                    return d;
                }
            }
        }
        return HEALTHCARE;
    }
}
