package com.sarvika.glsos.core;

import java.util.Collections;
import java.util.List;

/**
 * Localized result produced by {@link SarvikaEngine}. Carries the title, body,
 * recommended local providers, currency context and emergency info, already
 * adapted to the user's language + region.
 */
public final class ServiceResponse {

    private final Domain domain;
    private final Language language;
    private final Region region;
    private final String title;
    private final String body;
    private final List<String> providers;

    public ServiceResponse(Domain domain, Language language, Region region,
                           String title, String body, List<String> providers) {
        this.domain = domain;
        this.language = language;
        this.region = region;
        this.title = title;
        this.body = body;
        this.providers = providers == null ? Collections.emptyList() : providers;
    }

    public Domain domain() {
        return domain;
    }

    public Language language() {
        return language;
    }

    public Region region() {
        return region;
    }

    public String title() {
        return title;
    }

    public String body() {
        return body;
    }

    public List<String> providers() {
        return providers;
    }
}
