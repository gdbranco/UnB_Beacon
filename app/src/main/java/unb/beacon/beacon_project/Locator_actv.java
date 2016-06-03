package unb.beacon.beacon_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unb.beacon.beacon_project.Utilidades.Beacon;
import unb.beacon.beacon_project.Utilidades.Utilidades;

public class Locator_actv extends Activity {
    private BluetoothLeScanner mBluetoothScanner;
    //private Handler mHandler = new Handler();
    private ScanCallback scanCallback;
    private boolean hasBT;
    private TextView Locator_text;
    private ListView List_view;
    private int i = 0;
    private ArrayList<Beacon> lista_beacons;
    private ListView lv;
    private ArrayAdapter<Beacon> arrayAdapter;
    //private boolean isScan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        init();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopDiscovery();
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
        lista_beacons = new ArrayList<Beacon>();
        List_view = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<Beacon>(this,android.R.layout.simple_list_item_1, lista_beacons);
        List_view.setAdapter(arrayAdapter);
        startDiscovery();
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
            else {
                mBluetoothScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
                scanCallback = createScanCallback();
                ok = true;
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

                switch(frametype) {
                    case Utilidades.FRAME_TYPE_UID:
                        Integer power = new Byte(data[1]).intValue();
                        String name;
                        String id;
                        name = Beacon.getInstanceNSpace(data);
                        id = Beacon.getInstanceId(data);
                        Integer rssi = new Integer(result.getRssi());
                        Double distance = Utilidades.getDistance(rssi, power);
                        boolean achou = false;
                        for (Beacon b : lista_beacons) {
                            if (id.equals(b.id)) {
                                b.update(rssi, power, -1, distance);
                                arrayAdapter.notifyDataSetChanged();
                                achou = true;
                            }
                        }
                        if (!achou) {
                            lista_beacons.add(new Beacon(name, id, rssi.intValue(), power.intValue(), -1, distance));
                            arrayAdapter.notifyDataSetChanged();
                        }
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
        Locator_text.setText("Scanning");
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
        finish();
    }
}
