package unb.beacon.beacon_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.bluetooth.le.AdvertiseSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import unb.beacon.beacon_project.Utilidades.Utilidades;

public class Broadcast_actv extends Activity {
    private BluetoothLeAdvertiser adv;
    String instance;
    String namespace;
    int txpower;
    int admode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        init();
    }

    private void init()
    {
        instance = Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_INSTANCE);
        namespace = Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE);
        txpower = Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXPOWER);
        admode =  Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXMODE);
        Interface();
    }

    private void Interface()
    {
        TextView text = (TextView) findViewById(R.id.broadcast_texto);
        text.setText(String.format(Locale.US,"NAMESPACE: %s\nINSTANCE: %s\nPOWERLEVEL: %s\nADMODE: %s",
                namespace,
                instance,
                Utilidades.getPowerLevel(getApplicationContext(),txpower),
                Utilidades.getADMode(getApplicationContext(),admode)));
        startAdvertising();
    }

    public void onBackPressed()
    {
        stopAdvertising();
        finish();
    }

    private void startAdvertising()
    {
        AdvertiseSettings advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(admode)
                .setTxPowerLevel(txpower)
                .setConnectable(true)
                .build();

        byte[] serviceData = null;
        try {
            serviceData = buildServiceData();
        } catch (IOException e) {
           finish();
        }

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .addServiceData(Utilidades.SERVICE_UUID, serviceData)
                .addServiceUuid(Utilidades.SERVICE_UUID)
                .setIncludeTxPowerLevel(false)
                .setIncludeDeviceName(false)
                .build();

        BluetoothManager m = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = m.getAdapter();
        adv = mBluetoothAdapter.getBluetoothLeAdvertiser();
        adv.startAdvertising(advertiseSettings, advertiseData, null);
    }

    private void stopAdvertising() {
        adv.stopAdvertising(null);
    }

    private byte[] buildServiceData() throws IOException
    {
        byte btxpower = Utilidades.txPowerLevelToByteValue(txpower);
        byte[] bnamespace = Utilidades.toByteArray(namespace);
        byte[] binstance = Utilidades.toByteArray(instance);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(new byte[]{Utilidades.FRAME_TYPE_UID,btxpower});
        os.write(bnamespace);
        os.write(binstance);
        return os.toByteArray();
    }

}
