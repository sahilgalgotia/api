package com.sarvika.glsos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;

import com.sarvika.glsos.core.Session;
import com.sarvika.glsos.ui.DashboardActivity;
import com.sarvika.glsos.ui.LoginActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

/** Headless (Robolectric) test of the login -> dashboard flow. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class LoginFlowTest {

    @Test
    public void loginStoresSessionAndOpensDashboard() {
        ActivityController<LoginActivity> controller =
                Robolectric.buildActivity(LoginActivity.class).setup();
        LoginActivity activity = controller.get();

        EditText name = activity.findViewById(R.id.etName);
        name.setText("Sagar");

        Button login = activity.findViewById(R.id.btnLogin);
        login.performClick();

        // Session persisted
        Session session = new Session(ApplicationProvider.getApplicationContext());
        assertEquals("Sagar", session.name());
        assertNotNull(session.language());

        // Dashboard launched
        ShadowActivity shadow = Shadows.shadowOf(activity);
        Intent next = shadow.getNextStartedActivity();
        assertNotNull("expected an outgoing intent", next);
        assertEquals(DashboardActivity.class.getName(), next.getComponent().getClassName());
    }

    @Test
    public void emptyNameDoesNotLogIn() {
        ActivityController<LoginActivity> controller =
                Robolectric.buildActivity(LoginActivity.class).setup();
        LoginActivity activity = controller.get();

        Button login = activity.findViewById(R.id.btnLogin);
        login.performClick();

        ShadowActivity shadow = Shadows.shadowOf(activity);
        assertTrue("should not navigate with empty name",
                shadow.getNextStartedActivity() == null);
    }
}
