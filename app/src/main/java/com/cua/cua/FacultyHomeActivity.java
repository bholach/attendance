package com.cua.cua;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FacultyHomeActivity extends AppCompatActivity{

    ImageButton wifi,barcode,finger_print,other;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);

        getSupportActionBar().setTitle(R.string.faculty_home_page);
        wifi = findViewById(R.id.wifi);
        barcode = findViewById(R.id.barcode);
        finger_print = findViewById(R.id.finger_print);
        other = findViewById(R.id.others);

       wifi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openWifi();
           }
       });

       barcode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openBarCode();
           }
       });

       finger_print.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openFingerPrint();
           }
       });
       other.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openOther();
           }
       });
    }

    private void openFingerPrint() {
    }

    public void openWifi(){
        startActivity(new Intent(this,FacultyWifiPinActivity.class));
    }

    public void openBarCode(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                showAlert();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
            }
        }else{
            Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(in, 12);
        }

    }
    public void openOther(){

    }

    public void showAlert(){
        ActivityCompat.requestPermissions(FacultyHomeActivity.this,new String[]{Manifest.permission.CAMERA},100);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100 : {
                for (int i=0,len = permissions.length;i < len ;i++){
                    String permission = permissions[i];
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        boolean shotAgain = ActivityCompat.shouldShowRequestPermissionRationale(this,permission);
                        if(shotAgain){
                            showAlert();
                        }
                    }
                }
            }
        }
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
