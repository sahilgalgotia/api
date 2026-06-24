package com.sarvika.glsos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sarvika.glsos.R;
import com.sarvika.glsos.core.Language;
import com.sarvika.glsos.core.Region;
import com.sarvika.glsos.core.RegionRepository;
import com.sarvika.glsos.core.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry screen: collects the user's name plus region + language, which seed the
 * Global Geo + Multilingual context for the rest of the app.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etName;
    private Spinner spRegion;
    private Spinner spLanguage;
    private final List<Region> regions = new ArrayList<>(RegionRepository.all().values());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Session session = new Session(this);
        if (session.isLoggedIn()) {
            goToDashboard();
            return;
        }

        etName = findViewById(R.id.etName);
        spRegion = findViewById(R.id.spRegion);
        spLanguage = findViewById(R.id.spLanguage);
        Button btnLogin = findViewById(R.id.btnLogin);

        List<String> regionNames = new ArrayList<>();
        for (Region r : regions) {
            regionNames.add(r.countryName() + " (" + r.currencyCode() + ")");
        }
        spRegion.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, regionNames));

        List<String> langNames = new ArrayList<>();
        for (Language l : Language.values()) {
            langNames.add(l.selfName());
        }
        spLanguage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, langNames));

        btnLogin.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
                return;
            }
            Region region = regions.get(spRegion.getSelectedItemPosition());
            Language language = Language.values()[spLanguage.getSelectedItemPosition()];
            new Session(this).save(name, region.countryCode(), language);
            goToDashboard();
        });
    }

    private void goToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
