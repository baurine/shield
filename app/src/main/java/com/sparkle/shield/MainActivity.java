package com.sparkle.shield;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Switch;

import java.util.List;


public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private final Intent accessibilitySettingsIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    private Switch shieldSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateSwitchStatus();
    }

    private void updateSwitchStatus() {
        shieldSwitch.setChecked(serviceEnabled());
    }

    private void initViews() {
        shieldSwitch = (Switch) findViewById(R.id.shield_switch);
        shieldSwitch.setOnClickListener(this);
    }

    private boolean serviceEnabled() {
        boolean enabled = false;

        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_GENERIC);

        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/." + ShieldService.NAME)) {
                enabled = true;
                break;
            }
        }

        return enabled;
    }

    @Override
    public void onClick(View v) {
        startActivity(accessibilitySettingsIntent);
    }
}
