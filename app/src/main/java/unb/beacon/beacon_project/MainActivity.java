package unb.beacon.beacon_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import unb.beacon.beacon_project.Utilidades.Utilidades;


public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothLeAdvertiser adv;
    TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilidades.SharedPreferencesManager.getInstance(getApplicationContext());
        init();
    }

    public void init(){
        boolean OK = request_bluetooth();
        if(OK)
        {
            Interface();
        }
        else
        {
            texto = (TextView) findViewById(R.id.textView);
            texto.setText(Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE));
        }
    }

    private boolean request_bluetooth()
    {
        BluetoothManager m = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = m.getAdapter();
        if (mBluetoothAdapter == null) {
            Utilidades.showAlert("Erro", "Bluetooth não existe!",this);
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enablebt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.startActivityForResult(enablebt, REQUEST_ENABLE_BLUETOOTH);
            } else if (!mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                Utilidades.showAlert("Erro", "Bluetooth LE não suportado", this);
            } else {
                adv = mBluetoothAdapter.getBluetoothLeAdvertiser();
                return true;
            }
        }
        return false;
    }

    public void Interface()
    {
        texto = (TextView) findViewById(R.id.textView);
        Button broad_btn;
        Button locator_btn;
        ImageButton broad_preference_btn;
        broad_preference_btn = (ImageButton) findViewById(R.id.broadcast_preference_btn);
        locator_btn = (Button) findViewById(R.id.locator_btn);
        broad_btn = (Button) findViewById(R.id.broadcast_btn);
        if(broad_preference_btn!=null) {
            broad_preference_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), BroadcastP_actv.class);
                    startActivity(i);
                }
            });
        }
        if(broad_btn!=null) {
            broad_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Broadcast_actv.class);
                    startActivity(i);
                }
            });
        }
        if(locator_btn!=null) {
            locator_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Locator_actv.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
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
}
