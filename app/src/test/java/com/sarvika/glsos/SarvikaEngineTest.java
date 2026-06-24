package com.sarvika.glsos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sarvika.glsos.core.Domain;
import com.sarvika.glsos.core.Language;
import com.sarvika.glsos.core.Region;
import com.sarvika.glsos.core.RegionRepository;
import com.sarvika.glsos.core.SarvikaEngine;
import com.sarvika.glsos.core.ServiceResponse;

import org.junit.Test;

/** Pure-JVM tests for the SARVIKA core reasoning engine. */
public class SarvikaEngineTest {

    private final SarvikaEngine engine = new SarvikaEngine();

    @Test
    public void detectsEnglishByDefault() {
        assertEquals(Language.EN, engine.detectLanguage("I need a doctor"));
        assertEquals(Language.EN, engine.detectLanguage(""));
        assertEquals(Language.EN, engine.detectLanguage(null));
    }

    @Test
    public void detectsHindiFromDevanagari() {
        assertEquals(Language.HI, engine.detectLanguage("मुझे डॉक्टर चाहिए"));
    }

    @Test
    public void detectsTamil() {
        assertEquals(Language.TA, engine.detectLanguage("எனக்கு மருத்துவர் வேண்டும்"));
    }

    @Test
    public void detectsBengali() {
        assertEquals(Language.BN, engine.detectLanguage("আমার একজন ডাক্তার দরকার"));
    }

    @Test
    public void localizesHealthcareForIndiaInHindi() {
        Region india = RegionRepository.forCountry("IN");
        ServiceResponse r = engine.localize(Domain.HEALTHCARE, Language.HI, india);

        assertEquals("स्वास्थ्य", r.title());
        assertTrue("should include Indian emergency number", r.body().contains("112"));
        assertTrue("should include rupee symbol", r.body().contains("\u20B9"));
        assertTrue("should include a local provider", r.body().contains("Apollo Hospitals"));
        assertTrue("Hindi emergency label", r.body().contains("आपातकालीन"));
    }

    @Test
    public void localizesHealthcareForUsInEnglish() {
        Region us = RegionRepository.forCountry("US");
        ServiceResponse r = engine.localize(Domain.HEALTHCARE, Language.EN, us);

        assertEquals("Healthcare", r.title());
        assertTrue(r.body().contains("911"));
        assertTrue(r.body().contains("$"));
        assertTrue(r.body().contains("Mayo Clinic"));
    }

    @Test
    public void currencyAdaptsToRegion() {
        ServiceResponse in = engine.localize(Domain.COMMERCE, Language.EN, RegionRepository.forCountry("IN"));
        ServiceResponse us = engine.localize(Domain.COMMERCE, Language.EN, RegionRepository.forCountry("US"));
        assertTrue(in.body().contains("INR"));
        assertTrue(us.body().contains("USD"));
        assertTrue(in.body().contains("Flipkart"));
        assertTrue(us.body().contains("Amazon"));
    }

    @Test
    public void unknownCountryFallsBackToIndia() {
        Region r = RegionRepository.forCountry("ZZ");
        assertEquals("IN", r.countryCode());
    }

    @Test
    public void nonEmergencyDomainOmitsEmergencyLine() {
        ServiceResponse r = engine.localize(Domain.FOOD, Language.EN, RegionRepository.forCountry("IN"));
        assertTrue(!r.body().contains("Emergency"));
    }
}
