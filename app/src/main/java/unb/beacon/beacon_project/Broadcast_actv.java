package unb.beacon.beacon_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Broadcast_actv extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        init();
    }

    private void init()
    {
        Interface();
    }

    private void Interface()
    {

    }

    public void onBackPressed()
    {
        finish();
    }
}
