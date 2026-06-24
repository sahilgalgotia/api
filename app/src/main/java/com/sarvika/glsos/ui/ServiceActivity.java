package com.sarvika.glsos.ui;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sarvika.glsos.R;
import com.sarvika.glsos.core.Domain;
import com.sarvika.glsos.core.Language;
import com.sarvika.glsos.core.SarvikaEngine;
import com.sarvika.glsos.core.ServiceResponse;
import com.sarvika.glsos.core.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the localized response for a selected domain. A language picker lets the
 * user re-localize the same content live, demonstrating the multilingual engine.
 */
public class ServiceActivity extends AppCompatActivity {

    private final SarvikaEngine engine = new SarvikaEngine();
    private Domain domain;
    private Session session;
    private TextView tvTitle;
    private TextView tvBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        session = new Session(this);
        domain = Domain.fromKey(getIntent().getStringExtra(DashboardActivity.EXTRA_DOMAIN));

        tvTitle = findViewById(R.id.tvServiceTitle);
        tvBody = findViewById(R.id.tvServiceBody);
        Spinner spLanguage = findViewById(R.id.spServiceLanguage);

        List<String> langNames = new ArrayList<>();
        for (Language l : Language.values()) {
            langNames.add(l.selfName());
        }
        spLanguage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, langNames));
        spLanguage.setSelection(session.language().ordinal());

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Language language = Language.values()[position];
                session.setLanguage(language);
                render(language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        render(session.language());
    }

    private void render(Language language) {
        ServiceResponse response = engine.localize(domain, language, session.region());
        tvTitle.setText(response.title());
        tvBody.setText(response.body());
    }
}
