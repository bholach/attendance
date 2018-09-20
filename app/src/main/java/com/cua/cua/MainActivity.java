package com.cua.cua;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    TextInputLayout user_id,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_id = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
    }

    public void loginUser(View view) {
        user_id.setErrorEnabled(false);
        password.setErrorEnabled(false);
        String uid = user_id.getEditText().getText().toString();
        String pass = password.getEditText().getText().toString();

        if(uid.isEmpty()){
            user_id.setErrorEnabled(true);
            user_id.setError("please enter id");
        }else if (pass.isEmpty()){
            password.setErrorEnabled(true);
            password.setError("please fill password");
        }else{
            if(uid.toLowerCase().contains("e") && pass.equals("12345")){
                startActivity(new Intent(this,FacultyHomeActivity.class));
            }else if(pass.equals("12345")){
                Intent in = new Intent(this,StudentHomeActivity.class);
                in.putExtra("uid",uid);
                startActivity(in);
            }else{
                Snackbar.make(this.password,"Wrong id or password",Snackbar.LENGTH_LONG).show();
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
