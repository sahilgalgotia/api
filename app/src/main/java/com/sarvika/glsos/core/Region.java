package com.sarvika.glsos.core;

/**
 * A geo + economy context resolved by the Global Geo Intelligence layer.
 * Holds the currency and emergency information used to localize service output.
 */
public final class Region {

    private final String countryCode;
    private final String countryName;
    private final String currencyCode;
    private final String currencySymbol;
    private final String emergencyNumber;
    private final Language defaultLanguage;

    public Region(String countryCode, String countryName, String currencyCode,
                  String currencySymbol, String emergencyNumber, Language defaultLanguage) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.emergencyNumber = emergencyNumber;
        this.defaultLanguage = defaultLanguage;
    }

    public String countryCode() {
        return countryCode;
    }

    public String countryName() {
        return countryName;
    }

    public String currencyCode() {
        return currencyCode;
    }

    public String currencySymbol() {
        return currencySymbol;
    }

    public String emergencyNumber() {
        return emergencyNumber;
    }

    public Language defaultLanguage() {
        return defaultLanguage;
    }
}
