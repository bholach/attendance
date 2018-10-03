package com.cua.cua;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FacultyHomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ImageButton wifi,barcode,finger_print,other;
    TextView _id,t_date,current_class,dia_date;
    String date;
    String stud_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);

        getSupportActionBar().setTitle(R.string.faculty_home_page);
        date = "";stud_class="";
        wifi = findViewById(R.id.wifi);
        barcode = findViewById(R.id.barcode);
        t_date = findViewById(R.id.t_date);
        finger_print = findViewById(R.id.finger_print);
        other = findViewById(R.id.others);

        _id = findViewById(R.id.uid);
        current_class = findViewById(R.id.current_class);
        showPopup(); //show popup_options
        checkStoragePermission();
        checkStoragePermission();
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
        checkStoragePermission();
        Intent in = new Intent(this,FacultyWifiPinActivity.class);
        in.putExtra("class",stud_class);
        startActivity(in);
    }

    public void openBarCode(){
        //checking camera permission
            checkStoragePermission();
            checkStoragePermission();
            Intent in = new Intent(this,BarcodeAttendanceActivity.class);
            in.putExtra("class",stud_class);
            startActivity(in);
    }

    public void checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                askCameraPermission();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
            }
        }
    }
    public void checkStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                askStoragePermission();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            }
        }
    }
    public void openOther(){
    }

    public void askCameraPermission(){
        //requesting camera permission
        ActivityCompat.requestPermissions(FacultyHomeActivity.this,new String[]{Manifest.permission.CAMERA},100);
    }
    public void askStoragePermission(){
        ActivityCompat.requestPermissions(FacultyHomeActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       boolean flag = false;
        switch (requestCode) {
            case 100: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean shotAgain = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (shotAgain) {
                            flag = true;
                        }
                    }
                }
            }
            case 200: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean shotAgain = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (shotAgain) {
                          flag = true;
                        }
                    }
                }
            }
        }
            if(flag){
                finishAffinity();
            }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are You Sure");
        dialog.setMessage("Do you Want to Exit ?");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setCancelable(false);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year,month,dayOfMonth);
        setDate(cal);
    }

    private void setDate(final Calendar cal) {
    final DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
    date = df.format(cal.getTime());
    t_date.setText("Date : "+date);
    dia_date.setText(date);
    }

    public void showPopup(){
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.popup_menu_layout);
        Button done;
        ImageButton dpicker;
        final Spinner select_branch,select_semester,select_section;
        final TextView selected_date;

        dpicker = dialog.findViewById(R.id.choose_date);
        done = dialog.findViewById(R.id.set_data);
        selected_date = dialog.findViewById(R.id.selected_date);
        select_branch = dialog.findViewById(R.id.branch);
        select_semester = dialog.findViewById(R.id.semester);
        select_section = dialog.findViewById(R.id.section);
        dia_date = selected_date;

        dpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateChooserFragment fragment = new DateChooserFragment();
                fragment.show(getSupportFragmentManager(),"date");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!date.isEmpty()) {
                    setData(select_branch.getSelectedItem().toString(), select_semester.getSelectedItem().toString(),select_section.getSelectedItem().toString());
                    dialog.dismiss();
                }else
                    showAlert();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setData(String branch,String sem,String section) {
        String cur_class = branch+" "+sem+" ("+section+")";
        stud_class = branch+sem+"("+section+")";
        current_class.setText("Class : "+cur_class);
    }

    public void showAlert(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage("please select date !");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setCancelable(false);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
