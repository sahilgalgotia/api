package com.sarvika.glsos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ApplicationProvider;

import com.sarvika.glsos.core.Domain;
import com.sarvika.glsos.core.Language;
import com.sarvika.glsos.core.Session;
import com.sarvika.glsos.ui.DashboardActivity;
import com.sarvika.glsos.ui.LoginActivity;
import com.sarvika.glsos.ui.ServiceActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.GraphicsMode;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Renders each screen to a PNG using Robolectric's native graphics pipeline.
 * This produces real pixel output of the actual layouts (no emulator needed).
 * Output dir is overridable via the {@code screenshotDir} system property.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
public class ScreenshotTest {

    private static final int WIDTH = 1080;

    @Test
    public void renderLogin() throws Exception {
        capture(Robolectric.buildActivity(LoginActivity.class).setup().get(), "01_login.png");
    }

    @Test
    public void renderDashboard() throws Exception {
        new Session(ApplicationProvider.getApplicationContext()).save("Sagar", "IN", Language.HI);
        capture(Robolectric.buildActivity(DashboardActivity.class).setup().get(), "02_dashboard.png");
    }

    @Test
    public void renderServiceHindi() throws Exception {
        new Session(ApplicationProvider.getApplicationContext()).save("Sagar", "IN", Language.HI);
        Intent i = new Intent(ApplicationProvider.getApplicationContext(), ServiceActivity.class);
        i.putExtra(DashboardActivity.EXTRA_DOMAIN, Domain.HEALTHCARE.key());
        capture(Robolectric.buildActivity(ServiceActivity.class, i).setup().get(), "03_service_healthcare_hindi.png");
    }

    @Test
    public void renderServiceCommerceUsEnglish() throws Exception {
        new Session(ApplicationProvider.getApplicationContext()).save("Alex", "US", Language.EN);
        Intent i = new Intent(ApplicationProvider.getApplicationContext(), ServiceActivity.class);
        i.putExtra(DashboardActivity.EXTRA_DOMAIN, Domain.COMMERCE.key());
        capture(Robolectric.buildActivity(ServiceActivity.class, i).setup().get(), "04_service_commerce_us.png");
    }

    private void capture(Activity activity, String name) throws Exception {
        View root = activity.getWindow().getDecorView();
        int wSpec = View.MeasureSpec.makeMeasureSpec(WIDTH, View.MeasureSpec.EXACTLY);
        int hSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        root.measure(wSpec, hSpec);
        int height = Math.max(root.getMeasuredHeight(), 200);
        root.layout(0, 0, WIDTH, height);

        // Force children to a finite height too (NestedScrollView reports content height).
        if (root instanceof ViewGroup && ((ViewGroup) root).getChildCount() > 0) {
            root.measure(wSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
            root.layout(0, 0, WIDTH, height);
        }

        Bitmap bmp = Bitmap.createBitmap(WIDTH, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.parseColor("#EEF1F6"));
        root.draw(canvas);

        String dir = System.getProperty("screenshotDir", "build/screenshots");
        File out = new File(dir);
        if (!out.exists()) {
            out.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(new File(out, name))) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
    }
}
