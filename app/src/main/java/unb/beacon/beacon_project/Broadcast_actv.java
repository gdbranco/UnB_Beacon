package unb.beacon.beacon_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

import unb.beacon.beacon_project.Utilidades.Utilidades;

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
        TextView text = (TextView) findViewById(R.id.broadcast_texto);
        text.setText(String.format(Locale.US,"NAMESPACE: %s\nINSTANCE: %s\nPOWERLEVEL: %s\nADMODE: %s",
                Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE),
                Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_INSTANCE),
                Utilidades.getPowerLevel(getApplicationContext(),Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXPOWER)),
                Utilidades.getADMode(getApplicationContext(),Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXMODE))));
    }

    public void onBackPressed()
    {
        finish();
    }
}
