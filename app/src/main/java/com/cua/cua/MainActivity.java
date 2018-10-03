package com.cua.cua;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText user_id,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_id = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
    }

    public void loginUser(View view) {
        String uid = user_id.getText().toString();
        String pass = password.getText().toString();

        if(uid.isEmpty()){
            user_id.setError("please enter id");
        }else if (pass.isEmpty()){
            password.setError("please fill password");
        }else{
            if(uid.toLowerCase().contains("e") && pass.equals("12345")){
                startActivity(new Intent(this,FacultyHomeActivity.class));
            }else if(pass.equals("12345")){
                Intent in = new Intent(this,StudentHomeActivity.class);
                in.putExtra("uid",uid);
                startActivity(in);
            }else{
                //Snackbar.make(this.password,"Wrong id or password",Snackbar.LENGTH_LONG).show();
                Toast.makeText(this, "Wrong id or password", Toast.LENGTH_SHORT).show();
            }
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
}
