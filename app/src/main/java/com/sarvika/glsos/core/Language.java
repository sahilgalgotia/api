package com.sarvika.glsos.core;

/**
 * Languages supported by the SARVIKA Auto-Multilingual Engine.
 * Each language carries an ISO code and a self-name used for UI display.
 */
public enum Language {
    EN("en", "English"),
    HI("hi", "हिन्दी"),
    TA("ta", "தமிழ்"),
    TE("te", "తెలుగు"),
    BN("bn", "বাংলা"),
    MR("mr", "मराठी");

    private final String code;
    private final String selfName;

    Language(String code, String selfName) {
        this.code = code;
        this.selfName = selfName;
    }

    public String code() {
        return code;
    }

    public String selfName() {
        return selfName;
    }

    public static Language fromCode(String code) {
        if (code == null) {
            return EN;
        }
        String c = code.toLowerCase();
        for (Language l : values()) {
            if (l.code.equals(c)) {
                return l;
            }
        }
        return EN;
    }
}
