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
import android.bluetooth.le.AdvertiseCallback;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import unb.beacon.beacon_project.Utilidades.Utilidades;

public class Broadcast_actv extends Activity {
    private BluetoothLeAdvertiser adv;
    private AdvertiseCallback advertiseCallback;
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
        BluetoothManager m = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = m.getAdapter();
        adv = mBluetoothAdapter.getBluetoothLeAdvertiser();
        advertiseCallback = createAdvertiseCallback();
        Interface();
    }

    private void Interface()
    {
        TextView text = (TextView) findViewById(R.id.broadcast_texto);
        text.setText(String.format(Locale.US,"NAMESPACE: %s\nINSTANCE: %s\nPOWERLEVEL: %s\nADMODE: %s",
                Utilidades.hextoString(namespace),
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
        adv.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
    }

    private void stopAdvertising() {
        adv.stopAdvertising(advertiseCallback);
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

    private AdvertiseCallback createAdvertiseCallback() {
        return new AdvertiseCallback() {
            @Override
            public void onStartFailure(int errorCode) {
                switch (errorCode) {
                    case ADVERTISE_FAILED_DATA_TOO_LARGE:
                        showToast("ADVERTISE_FAILED_DATA_TOO_LARGE");
                        break;
                    case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                        showToast("ADVERTISE_FAILED_TOO_MANY_ADVERTISERS");
                        break;
                    case ADVERTISE_FAILED_ALREADY_STARTED:
                        showToast("ADVERTISE_FAILED_ALREADY_STARTED");
                        break;
                    case ADVERTISE_FAILED_INTERNAL_ERROR:
                        showToast("ADVERTISE_FAILED_INTERNAL_ERROR");
                        break;
                    case ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                        showToast("ADVERTISE_FAILED_FEATURE_UNSUPPORTED");
                        break;
                    default:
                        showToast("startAdvertising failed with unknown error " + errorCode);
                        break;
                }
            }
        };
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
