package com.suelee.maps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RadioGroup radioGroup = findViewById(R.id.rg);
        RadioButton rbNormal = findViewById(R.id.normal);
        RadioButton rbSatellite = findViewById(R.id.satellite);
        RadioButton rbTerrain = findViewById(R.id.terrain);
        RadioButton rbBybrid = findViewById(R.id.bybrid);
        if (GoogleMapHelper.MapType == GoogleMap.MAP_TYPE_NORMAL) {
            rbNormal.setChecked(true);
        } else if (GoogleMapHelper.MapType == GoogleMap.MAP_TYPE_SATELLITE) {
            rbSatellite.setChecked(true);
        } else if (GoogleMapHelper.MapType == GoogleMap.MAP_TYPE_TERRAIN) {
            rbTerrain.setChecked(true);
        } else if (GoogleMapHelper.MapType == GoogleMap.MAP_TYPE_HYBRID) {
            rbBybrid.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.normal:
                        GoogleMapHelper.MapType = GoogleMap.MAP_TYPE_NORMAL;
                        break;
                    case R.id.satellite:
                        GoogleMapHelper.MapType = GoogleMap.MAP_TYPE_SATELLITE;
                        break;
                    case R.id.terrain:
                        GoogleMapHelper.MapType = GoogleMap.MAP_TYPE_TERRAIN;
                        break;
                    case R.id.bybrid:
                        GoogleMapHelper.MapType = GoogleMap.MAP_TYPE_HYBRID;
                        break;
                }
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch aSwitch = findViewById(R.id.mode_switch);
        aSwitch.setChecked(GoogleMapHelper.indoorEnabled);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                GoogleMapHelper.indoorEnabled = b;
            }
        });
    }
}