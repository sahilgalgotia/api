package com.sarvika.glsos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.sarvika.glsos.core.Domain;
import com.sarvika.glsos.core.Language;
import com.sarvika.glsos.core.Session;
import com.sarvika.glsos.ui.DashboardActivity;
import com.sarvika.glsos.ui.ServiceActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

/** Headless tests of the dashboard -> localized service flow. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class ServiceFlowTest {

    @Before
    public void seedSession() {
        new Session(ApplicationProvider.getApplicationContext())
                .save("Sagar", "IN", Language.HI);
    }

    @Test
    public void dashboardGreetsUserAndOpensDomain() {
        DashboardActivity activity =
                Robolectric.buildActivity(DashboardActivity.class).setup().get();

        TextView tvName = activity.findViewById(R.id.tvName);
        assertTrue("greeting should contain the user name", tvName.getText().toString().contains("Sagar"));
        assertTrue("greeting should contain the region", tvName.getText().toString().contains("India"));

        activity.findViewById(R.id.cardHealthcare).performClick();

        ShadowActivity shadow = Shadows.shadowOf(activity);
        Intent next = shadow.getNextStartedActivity();
        assertNotNull(next);
        assertEquals(ServiceActivity.class.getName(), next.getComponent().getClassName());
        assertEquals(Domain.HEALTHCARE.key(), next.getStringExtra(DashboardActivity.EXTRA_DOMAIN));
    }

    @Test
    public void serviceShowsLocalizedHindiContent() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ServiceActivity.class);
        intent.putExtra(DashboardActivity.EXTRA_DOMAIN, Domain.HEALTHCARE.key());

        ServiceActivity activity =
                Robolectric.buildActivity(ServiceActivity.class, intent).setup().get();

        TextView title = activity.findViewById(R.id.tvServiceTitle);
        TextView body = activity.findViewById(R.id.tvServiceBody);

        assertEquals("स्वास्थ्य", title.getText().toString());
        assertTrue(body.getText().toString().contains("112"));
        assertTrue(body.getText().toString().contains("\u20B9"));
        assertTrue(body.getText().toString().contains("Apollo Hospitals"));
    }
}
