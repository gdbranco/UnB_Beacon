package unb.beacon.beacon_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
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
    private boolean hasBT;
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

    private boolean request_bluetooth()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean ok = false;
        if (mBluetoothAdapter == null) {
            Utilidades.showAlert("Erro", "Bluetooth não existe!",this);
        } else {
            if (!mBluetoothAdapter.isEnabled())
            {
                Intent enablebt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.startActivityForResult(enablebt, Utilidades.REQUEST_ENABLE_BLUETOOTH);
            }
            else
            {
                adv = mBluetoothAdapter.getBluetoothLeAdvertiser();
                advertiseCallback = createAdvertiseCallback();
                ok = true;
            }
            if (!mBluetoothAdapter.isMultipleAdvertisementSupported())
            {
                Utilidades.showAlert("Erro", "Bluetooth LE MULTIAD não suportado", this);
            }
        }
        return ok;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Utilidades.REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    init();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void init()
    {
        hasBT = request_bluetooth();
        if(hasBT)
        {
            instance = Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_INSTANCE);
            namespace = Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE);
            txpower = Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXPOWER);
            admode =  Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXMODE);
            Interface();
        }
    }

    private void Interface()
    {
        TextView text = (TextView) findViewById(R.id.broadcast_texto);
        text.setText(String.format("NAMESPACE: %s\nINSTANCE: %s\nPOWERLEVEL: %s\nADMODE: %s",
                namespace,
                instance,
                Utilidades.getPowerLevel(getApplicationContext(),txpower),
                Utilidades.getADMode(getApplicationContext(),admode)));
        startAdvertising();
    }

    public void onBackPressed()
    {
        if(hasBT)
        {
            stopAdvertising();
        }
        finish();
    }

    private void startAdvertising()
    {
        showToast(String.format("NAMESPACE: %s\nINSTANCE: %s\nPOWERLEVEL: %s\nADMODE: %s",
                namespace,
                instance,
                Utilidades.getPowerLevel(getApplicationContext(),txpower),
                Utilidades.getADMode(getApplicationContext(),admode)));
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
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
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
        os.write(new byte[]{Utilidades.FRAME_TYPE_UID,btxpower}); // 2byte
        os.write(bnamespace); // 10 bytes
        os.write(binstance); // 6 bytes
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
