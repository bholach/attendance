package com.cua.cua;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BarcodeAttendanceActivity extends AppCompatActivity {

    ListView student_list_view;
    ImageButton scan_btn;
    List<String> stud_data;
    ArrayAdapter<String> adapter;
    String stud_class ="data";
    boolean saved ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_attendance);
        saved = false;
        stud_class = getIntent().getStringExtra("class");
        stud_data = new ArrayList<>();

        student_list_view = findViewById(R.id.student_list_view);
        scan_btn = findViewById(R.id.scan_btn);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,stud_data);

        student_list_view.setAdapter(adapter);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // new IntentIntegrator(BarcodeAttendanceActivity.this).initiateScan();
                IntentIntegrator integrator = new IntentIntegrator(BarcodeAttendanceActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(!saved){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Are You Sure");
            dialog.setMessage("Exit without saving ?");
            dialog.setIcon(R.drawable.ic_warning);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else{
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.barcode_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save: saveFile(); return true;
            default: return false;
        }
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                stud_data.add(result.getContents());
                adapter.notifyDataSetChanged();
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //save method to save file to storage
    protected void saveFile(){

        File root = android.os.Environment.getExternalStorageDirectory();
        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
        File dir = new File (root.getAbsolutePath() + "/attendance");
        dir.mkdirs();
        File file = new File(dir, stud_class+".csv");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("UID");
            for(String s:stud_data){
               pw.println(s);
            }
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Toast.makeText(this, "Fail to save file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //e.printStackTrace();
            Toast.makeText(this, "Some error occured ", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "File Saved to "+file, Toast.LENGTH_LONG).show();
        saved = true;
    }
}
