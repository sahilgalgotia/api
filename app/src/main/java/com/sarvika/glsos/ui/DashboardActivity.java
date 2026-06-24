package com.sarvika.glsos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.sarvika.glsos.R;
import com.sarvika.glsos.core.Domain;
import com.sarvika.glsos.core.Session;

/**
 * Home dashboard listing the localized life-service domains. Tapping a card
 * opens {@link ServiceActivity} for that domain.
 */
public class DashboardActivity extends AppCompatActivity {

    public static final String EXTRA_DOMAIN = "extra_domain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Session session = new Session(this);
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(getString(R.string.welcome_user, session.name(),
                session.region().countryName()));

        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            session.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        bindDomain(R.id.cardHealthcare, Domain.HEALTHCARE);
        bindDomain(R.id.cardFood, Domain.FOOD);
        bindDomain(R.id.cardCommerce, Domain.COMMERCE);
        bindDomain(R.id.cardEmployment, Domain.EMPLOYMENT);
        bindDomain(R.id.cardMarriage, Domain.MARRIAGE);
        bindDomain(R.id.cardMicro, Domain.MICROSERVICES);
        bindDomain(R.id.cardEducation, Domain.EDUCATION);
        bindDomain(R.id.cardFinance, Domain.FINANCE);
    }

    private void bindDomain(int viewId, Domain domain) {
        View v = findViewById(viewId);
        if (v != null) {
            v.setOnClickListener(view -> {
                Intent i = new Intent(this, ServiceActivity.class);
                i.putExtra(EXTRA_DOMAIN, domain.key());
                startActivity(i);
            });
        }
    }
}
