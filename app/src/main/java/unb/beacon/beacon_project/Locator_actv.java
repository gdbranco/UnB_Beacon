package unb.beacon.beacon_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.widget.TextView;

import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;

import unb.beacon.beacon_project.Utilidades.Utilidades;

public class Locator_actv extends Activity {
    private BluetoothLeScanner mBluetoothScanner;
    private Handler mHandler = new Handler();
    private ScanCallback scanCallback;
    private boolean hasBT;
    private TextView Locator_text;
    private boolean isScan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        init();
    }

    private void init()
    {
        hasBT = request_bluetooth();
        if(hasBT)
        {
            Interface();
        }
    }

    private void Interface()
    {
        Locator_text = (TextView) findViewById(R.id.locator_text);
        mHandler.post(scanRun);
    }

    private Runnable scanRun = new Runnable() {
        @Override
        public void run() {
            if(isScan)
            {
                stopDiscovery();
            }
            else
            {
                startDiscovery();
            }
            isScan = !isScan;
            mHandler.postDelayed(this,500);
        }
    };

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
                mBluetoothScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
                scanCallback = createScanCallback();
                ok = true;
            }
            if (!mBluetoothAdapter.isMultipleAdvertisementSupported())
            {
                Utilidades.showAlert("Erro", "Bluetooth LE MULTIAD não suportado", this);
            }
        }
        return ok;
    }

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

    private ScanCallback createScanCallback () {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                if (result == null
                        || result.getDevice() == null){
                    if(result == null){ Locator_text.setText("NADA RESULT"); }
                    if(result.getDevice() == null){ Locator_text.setText("NADA GETDEVICE"); }
                    return;}

                byte[] data = result.getScanRecord().getServiceData(Utilidades.SERVICE_UUID);

                byte frametype = data[0];

                switch(frametype)
                {
                    case Utilidades.FRAME_TYPE_UID:
                        StringBuilder b = new StringBuilder();
                        //POWER FICA NO SEGUNDO BYTE
                        Integer power = new Byte(data[1]).intValue();
                        //NAMESPACE FICA NO MEIO
                        String name;
                        for(int i = 2;i<12;i++)
                        {
                            b.append(Integer.toHexString(data[i] & 0xFF));
                        }
                        name = b.toString();
                        b = new StringBuilder();
                        //ID FICA NOS UTLIMOS 6 BYTES DO DATA
                        String id;
                        for(int i = 12;i<18;i++)
                        {
                            b.append(Integer.toHexString(data[i] & 0xFF));
                        }
                        id = b.toString();
                        Integer rssi = new Integer(result.getRssi());
                        Double distance = Utilidades.getDistance(rssi,power);
                        Locator_text.setText(String.format("NAMESPACE: %s\nID: %s\nTXPOWER: %d\nRSSI: %d\nDISTANCIA: %.2f\n",Utilidades.hextoString(name),id,power,rssi,distance));
                        break;
                }

            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }

    private void startDiscovery()
    {
        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid(Utilidades.SERVICE_UUID)
                .build();
        filters.add(filter);

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        mBluetoothScanner.startScan(filters,settings,scanCallback);
    }

    private void stopDiscovery()
    {
        mBluetoothScanner.stopScan(scanCallback);
    }

    public void onBackPressed()
    {
        if(hasBT)
        {
            stopDiscovery();
        }
        finish();
    }
}
