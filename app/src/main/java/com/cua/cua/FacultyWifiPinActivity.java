package com.cua.cua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class FacultyWifiPinActivity extends AppCompatActivity {

    TextView pin;
    Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_wifi_pin);

        getSupportActionBar().setTitle(R.string.wifi_pin_page_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pin = findViewById(R.id.pin);
        random = new Random();
        int num = 10000000 + random.nextInt(90000000);
        String pin_data = "PIN : "+num;
        pin.setText(pin_data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return  true;
    }
}
