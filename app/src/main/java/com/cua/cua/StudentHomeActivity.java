package com.cua.cua;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StudentHomeActivity extends AppCompatActivity {

    TextView name,uid;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    IntentFilter mIntentFilter;
    WifiManager mywifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        getSupportActionBar().setTitle(R.string.stud_home_page_name);
        name = findViewById(R.id.name);
        uid = findViewById(R.id.uid);
        String data = "UID : "+getIntent().getStringExtra("uid");
        uid.setText(data);

        //intent filters
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mywifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mywifi != null && !mywifi.isWifiEnabled());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            showAlert("","Contact Faculty");
        }

        mManager = (WifiP2pManager) getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
            }
        });

        //For setting Device Name
        Class[] paramsTypes = new Class[3];
        paramsTypes[0] = WifiP2pManager.Channel.class;
        paramsTypes[1] = String.class;
        paramsTypes[2] = WifiP2pManager.ActionListener.class;

        try {
            Method setDeviceName = mManager.getClass().getMethod("setDeviceName",paramsTypes);
            setDeviceName.setAccessible(true);
            Object argsList[] = new Object[3];
            argsList[0] = mChannel;
            argsList[1] = getIntent().getStringExtra("uid");
            argsList[2] = new WifiP2pManager.ActionListener(){

                @Override
                public void onSuccess() {}
                @Override
                public void onFailure(int reason) {
                    showAlert("","Restart App");
                }
            };
            setDeviceName.invoke(mManager, argsList);
        } catch (NoSuchMethodException e) {
           showAlert("","Contact Faculty");
        } catch (IllegalAccessException e) {
            showAlert("","Contact Faculty");
        } catch (InvocationTargetException e) {
            showAlert("","Contact Faculty");
        }

    }

    public void attendNow(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Attending Attendance");
        dialog.setMessage("Please Wait...");
        dialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showAlert("Completed","Attendance Done");
            }
        });
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                dialog.show();
            }
            @Override
            public void onFailure(int reasonCode) {
               showAlert("","W8769");
            }
        });

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
                closeWifi();
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

    private void closeWifi() {
        if(mywifi !=null && mywifi.isWifiEnabled())
            mywifi.setWifiEnabled(false);
    }

    public void showAlert(String type,String data){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        if(type.isEmpty()){
            type = "Error";
            dialog.setIcon(R.drawable.ic_warning);
        }else
            dialog.setIcon(R.drawable.ic_verified);

        dialog.setTitle(type);
        dialog.setMessage(data);
        dialog.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeWifi();
                finishAffinity();
            }
        });
        dialog.show();
    }
}
