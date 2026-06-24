package com.sarvika.glsos.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simulated Global Geo + Cross-Economy registry. Maps an ISO country code to a
 * {@link Region} carrying currency and emergency context. In a production GLSOS
 * this would be backed by a real geo/IP + compliance service.
 */
public final class RegionRepository {

    private static final Map<String, Region> REGIONS = build();

    private RegionRepository() {
    }

    private static Map<String, Region> build() {
        Map<String, Region> m = new LinkedHashMap<>();
        m.put("IN", new Region("IN", "India", "INR", "\u20B9", "112", Language.HI));
        m.put("US", new Region("US", "United States", "USD", "$", "911", Language.EN));
        m.put("GB", new Region("GB", "United Kingdom", "GBP", "\u00A3", "999", Language.EN));
        m.put("DE", new Region("DE", "Germany", "EUR", "\u20AC", "112", Language.EN));
        return Collections.unmodifiableMap(m);
    }

    /** Returns the region for the given country code, defaulting to India. */
    public static Region forCountry(String countryCode) {
        if (countryCode != null) {
            Region r = REGIONS.get(countryCode.toUpperCase());
            if (r != null) {
                return r;
            }
        }
        return REGIONS.get("IN");
    }

    public static Map<String, Region> all() {
        return REGIONS;
    }
}
