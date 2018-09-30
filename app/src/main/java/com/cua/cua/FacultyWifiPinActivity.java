package com.cua.cua;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cua.cua.receivers.WiFiDirectBroadcastReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FacultyWifiPinActivity extends AppCompatActivity {

    TextView pin;
    ListView uid_list;
    boolean saved;
    String stud_class;

    List<WifiP2pDevice> peerDevices;
    ArrayList<String> deviceList;
    ArrayAdapter<String> adapter;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    IntentFilter mIntentFilter;
    WifiManager mywifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_wifi_pin);

        getSupportActionBar().setTitle(R.string.wifi_pin_page_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saved = false;
        stud_class = getIntent().getStringExtra("class");
        uid_list = findViewById(R.id.uid_list);
        pin = findViewById(R.id.pin);

        peerDevices = new ArrayList<>();
        deviceList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,deviceList);
        uid_list.setAdapter(adapter);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //for p2p
        getPermission();

        mywifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mywifi != null && !mywifi.isWifiEnabled())
            mywifi.setWifiEnabled(true);

        mManager = (WifiP2pManager) getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
            }
        });
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        closeWifi();
        onBackPressed();
        return  true;
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

    BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

            } else {
                if(mywifi !=null) mywifi.setWifiEnabled(true);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        if(!peers.getDeviceList().equals(peerDevices)){
                            peerDevices.clear();
                            peerDevices.addAll(peers.getDeviceList());

                            for(WifiP2pDevice device : peers.getDeviceList()){
                                deviceList.add(device.deviceName);
                            }
                            //Collections.sort(deviceList);
                            String count = pin.getText().toString()+"\nTotal : "+deviceList.size();
                            pin.setText(count);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
 }  ;

    public void startPeer(View view) {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(FacultyWifiPinActivity.this, "Taking Attendance...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(FacultyWifiPinActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                askFineLocationPermission();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            }
        }
        else{
            //Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }
    }
    private void askFineLocationPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { //funcitno executes when some permission was granted
        switch (requestCode){
            case 100 : {
                for (int i=0,len = permissions.length;i < len ;i++){
                    String permission = permissions[i];
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        boolean shotAgain = ActivityCompat.shouldShowRequestPermissionRationale(this,permission);
                        if(shotAgain){
                            askFineLocationPermission();
                        }
                    }
                }
            }
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
            for(String s:deviceList){
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
    private void closeWifi() {
        if(mywifi !=null && mywifi.isWifiEnabled())
            mywifi.setWifiEnabled(false);
    }
}

