package com.sarvika.glsos.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Lightweight persistence for the user's session: name plus the resolved
 * region and language preferences used by the localization engine.
 */
public final class Session {

    private static final String PREFS = "sarvika_session";
    private static final String KEY_NAME = "name";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences prefs;

    public Session(Context context) {
        this.prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String name, String countryCode, Language language) {
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_COUNTRY, countryCode)
                .putString(KEY_LANGUAGE, language.code())
                .apply();
    }

    public void setLanguage(Language language) {
        prefs.edit().putString(KEY_LANGUAGE, language.code()).apply();
    }

    public boolean isLoggedIn() {
        return prefs.contains(KEY_NAME);
    }

    public String name() {
        return prefs.getString(KEY_NAME, "");
    }

    public String countryCode() {
        return prefs.getString(KEY_COUNTRY, "IN");
    }

    public Region region() {
        return RegionRepository.forCountry(countryCode());
    }

    public Language language() {
        return Language.fromCode(prefs.getString(KEY_LANGUAGE, "en"));
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
