package com.cua.cua;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StudentHomeActivity extends AppCompatActivity {

    TextView name,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        getSupportActionBar().setTitle(R.string.stud_home_page_name);
        name = findViewById(R.id.name);
        uid = findViewById(R.id.uid);

        String data = "UID : "+getIntent().getStringExtra("uid");
        uid.setText(data);

    }

    public void searchWifi(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Searching Wifi");
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are You Sure");
        dialog.setMessage("Do you Want to Exit ?");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
