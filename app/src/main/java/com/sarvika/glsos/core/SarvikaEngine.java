package com.sarvika.glsos.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * SARVIKA / GLSOS core reasoning engine.
 *
 * <p>Implements a compact version of the documented execution flow:
 * detect language &rarr; resolve geo/region &rarr; select domain &rarr;
 * localize content &rarr; return a localized {@link ServiceResponse}.</p>
 *
 * <p>This class is pure Java (no Android dependencies) so it can be unit-tested
 * on the JVM and reused by any front-end.</p>
 */
public final class SarvikaEngine {

    /**
     * STEP 1 of the multilingual engine: detect the script/language of free
     * text by inspecting Unicode blocks. Falls back to English.
     */
    public Language detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Language.EN;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 0x0B80 && c <= 0x0BFF) {
                return Language.TA; // Tamil
            }
            if (c >= 0x0C00 && c <= 0x0C7F) {
                return Language.TE; // Telugu
            }
            if (c >= 0x0980 && c <= 0x09FF) {
                return Language.BN; // Bengali
            }
            if (c >= 0x0900 && c <= 0x097F) {
                return Language.HI; // Devanagari (Hindi / Marathi)
            }
        }
        return Language.EN;
    }

    /**
     * Geo + Language + Domain fusion: produce a localized response for the
     * selected domain, adapted to the user's language and region.
     */
    public ServiceResponse localize(Domain domain, Language language, Region region) {
        Labels labels = Labels.forLanguage(language);
        String title = domainTitle(domain, language);
        List<String> providers = providersFor(domain, region);

        StringBuilder body = new StringBuilder();
        body.append(labels.region).append(": ").append(region.countryName()).append('\n');
        body.append(labels.providers).append(": ").append(String.join(", ", providers)).append('\n');
        body.append(labels.currency).append(": ")
                .append(region.currencySymbol()).append(" (").append(region.currencyCode()).append(')');

        if (domain == Domain.HEALTHCARE) {
            body.append('\n').append(labels.emergency).append(": ").append(region.emergencyNumber());
        }

        return new ServiceResponse(domain, language, region, title, body.toString(), providers);
    }

    private String domainTitle(Domain domain, Language language) {
        Map<Language, String> byLang = DOMAIN_TITLES.get(domain);
        String t = byLang == null ? null : byLang.get(language);
        if (t == null && byLang != null) {
            t = byLang.get(Language.EN); // graceful fallback
        }
        return t == null ? domain.key() : t;
    }

    private List<String> providersFor(Domain domain, Region region) {
        Map<Domain, List<String>> byDomain = PROVIDERS.get(region.countryCode());
        if (byDomain != null && byDomain.containsKey(domain)) {
            return byDomain.get(domain);
        }
        return new ArrayList<>(Arrays.asList("Local Provider A", "Local Provider B"));
    }

    // ----- Static localization data (a real GLSOS would load this remotely) -----

    private static final Map<Domain, Map<Language, String>> DOMAIN_TITLES = buildTitles();
    private static final Map<String, Map<Domain, List<String>>> PROVIDERS = buildProviders();

    private static Map<Domain, Map<Language, String>> buildTitles() {
        Map<Domain, Map<Language, String>> m = new EnumMap<>(Domain.class);
        m.put(Domain.HEALTHCARE, titles("Healthcare", "\u0938\u094D\u0935\u093E\u0938\u094D\u0925\u094D\u092F", "\u0B9A\u0BC1\u0B95\u0BBE\u0BA4\u0BBE\u0BB0\u0BAE\u0BCD"));
        m.put(Domain.FOOD, titles("Food", "\u092D\u094B\u091C\u0928", "\u0B89\u0BA3\u0BB5\u0BC1"));
        m.put(Domain.COMMERCE, titles("Commerce", "\u0935\u093E\u0923\u093F\u091C\u094D\u092F", "\u0BB5\u0BBE\u0BA3\u0BBF\u0B95\u0BAE\u0BCD"));
        m.put(Domain.EMPLOYMENT, titles("Employment", "\u0930\u094B\u091C\u0917\u093E\u0930", "\u0BB5\u0BC7\u0BB2\u0BC8"));
        m.put(Domain.MARRIAGE, titles("Marriage", "\u0935\u093F\u0935\u093E\u0939", "\u0BA4\u0BBF\u0BB0\u0BAE\u0BA3\u0BAE\u0BCD"));
        m.put(Domain.MICROSERVICES, titles("MicroServices", "\u0938\u0942\u0915\u094D\u0937\u094D\u092E \u0938\u0947\u0935\u093E\u090F\u0901", "\u0B9A\u0BBF\u0BB1\u0BC1 \u0B9A\u0BC7\u0BB5\u0BC8\u0B95\u0BB3\u0BCD"));
        m.put(Domain.EDUCATION, titles("Education", "\u0936\u093F\u0915\u094D\u0937\u093E", "\u0B95\u0BB2\u0BCD\u0BB5\u0BBF"));
        m.put(Domain.FINANCE, titles("Finance", "\u0935\u093F\u0924\u094D\u0924", "\u0BA8\u0BBF\u0BA4\u0BBF"));
        return m;
    }

    private static Map<Language, String> titles(String en, String hi, String ta) {
        Map<Language, String> m = new EnumMap<>(Language.class);
        m.put(Language.EN, en);
        m.put(Language.HI, hi);
        m.put(Language.MR, hi); // Marathi shares Devanagari content here
        m.put(Language.TA, ta);
        return m;
    }

    private static Map<String, Map<Domain, List<String>>> buildProviders() {
        Map<String, Map<Domain, List<String>>> m = new java.util.HashMap<>();

        Map<Domain, List<String>> in = new EnumMap<>(Domain.class);
        in.put(Domain.HEALTHCARE, Arrays.asList("Apollo Hospitals", "Fortis", "AIIMS"));
        in.put(Domain.FOOD, Arrays.asList("Zomato", "Swiggy"));
        in.put(Domain.COMMERCE, Arrays.asList("Flipkart", "Amazon.in", "Meesho"));
        in.put(Domain.EMPLOYMENT, Arrays.asList("Naukri", "Apna", "LinkedIn"));
        in.put(Domain.EDUCATION, Arrays.asList("BYJU'S", "Unacademy"));
        in.put(Domain.FINANCE, Arrays.asList("UPI", "Paytm", "Zerodha"));
        m.put("IN", in);

        Map<Domain, List<String>> us = new EnumMap<>(Domain.class);
        us.put(Domain.HEALTHCARE, Arrays.asList("Mayo Clinic", "Cleveland Clinic"));
        us.put(Domain.FOOD, Arrays.asList("DoorDash", "Uber Eats"));
        us.put(Domain.COMMERCE, Arrays.asList("Amazon", "Walmart", "eBay"));
        us.put(Domain.EMPLOYMENT, Arrays.asList("Indeed", "LinkedIn"));
        us.put(Domain.EDUCATION, Arrays.asList("Coursera", "Khan Academy"));
        us.put(Domain.FINANCE, Arrays.asList("Chase", "Robinhood"));
        m.put("US", us);

        return m;
    }

    /** Localized UI labels for the assembled response body. */
    private static final class Labels {
        final String region;
        final String providers;
        final String currency;
        final String emergency;

        private Labels(String region, String providers, String currency, String emergency) {
            this.region = region;
            this.providers = providers;
            this.currency = currency;
            this.emergency = emergency;
        }

        static Labels forLanguage(Language language) {
            switch (language) {
                case HI:
                case MR:
                    return new Labels(
                            "\u0915\u094D\u0937\u0947\u0924\u094D\u0930",          // क्षेत्र
                            "\u0928\u091C\u0926\u0940\u0915\u0940 \u0938\u0947\u0935\u093E\u090F\u0901", // नजदीकी सेवाएँ
                            "\u092E\u0941\u0926\u094D\u0930\u093E",                // मुद्रा
                            "\u0906\u092A\u093E\u0924\u0915\u093E\u0932\u0940\u0928"); // आपातकालीन
                case TA:
                    return new Labels(
                            "\u0BAA\u0B95\u0BC1\u0BA4\u0BBF",                      // பகுதி
                            "\u0B85\u0BB0\u0BC1\u0B95\u0BBF\u0BB2\u0BC1\u0BB3\u0BCD\u0BB3 \u0B9A\u0BC7\u0BB5\u0BC8\u0B95\u0BB3\u0BCD", // அருகிலுள்ள சேவைகள்
                            "\u0BA8\u0BBE\u0BA3\u0BAF\u0BAE\u0BCD",                // நாணயம்
                            "\u0B85\u0BB5\u0B9A\u0BB0\u0BAE\u0BCD");               // அவசரம்
                default:
                    return new Labels("Region", "Nearby providers", "Currency", "Emergency");
            }
        }
    }
}
