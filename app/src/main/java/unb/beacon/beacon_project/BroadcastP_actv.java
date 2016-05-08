package unb.beacon.beacon_project;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import unb.beacon.beacon_project.Utilidades.Utilidades;

public class BroadcastP_actv extends Activity {
    private EditText namespace;
    private EditText instance;
    int txPowerLevel;
    int adMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_p_actv);
        init();
    }

    private void init() {
        Interface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(namespace != null && instance != null){
            Utilidades.SharedPreferencesManager.getInstance().putString(Utilidades.P_NAMESPACE,namespace.getText().toString());
            Utilidades.SharedPreferencesManager.getInstance().putString(Utilidades.P_INSTANCE,instance.getText().toString());
            Utilidades.SharedPreferencesManager.getInstance().putInt(Utilidades.P_TXPOWER, txPowerLevel);
            Utilidades.SharedPreferencesManager.getInstance().putInt(Utilidades.P_TXMODE, adMode);
        }
    }

    private void Interface()
    {
        Button rnd_namespace;
        Button rnd_instance;
        Spinner txpower;
        Spinner txmode;
        txPowerLevel = Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXPOWER);
        adMode = Utilidades.SharedPreferencesManager.getInstance().getInt(Utilidades.P_TXMODE);
        namespace = (EditText) findViewById(R.id.namespace_string);
        namespace.setText(Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE));
        instance = (EditText) findViewById(R.id.instance_string);
        instance.setText(Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_INSTANCE));
        rnd_namespace = (Button) findViewById(R.id.rnd_btn1);
        if(rnd_namespace!=null) {
            rnd_namespace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    namespace.setText(Utilidades.randomHexString(10));
                }
            });
        }
        rnd_instance = (Button) findViewById(R.id.rnd_btn2);
        if(rnd_instance!=null) {
            rnd_instance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instance.setText(Utilidades.randomHexString(6));
                }
            });
        }
        txpower = (Spinner) findViewById(R.id.txpower_spnr);
        ArrayAdapter<CharSequence> txpowerAdapter = ArrayAdapter.createFromResource(this,R.array.tx_power_array,android.R.layout.simple_spinner_dropdown_item);
        txpowerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txpower.setAdapter(txpowerAdapter);
        txpower.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                txPowerLevel = Utilidades.getPowerLevel(getApplicationContext(),selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // NOP
            }
        });
        txpower.setSelection(txPowerLevel);
        txmode = (Spinner) findViewById(R.id.txmode_spnr);
        ArrayAdapter<CharSequence> txmodeAdapter = ArrayAdapter.createFromResource(this,R.array.tx_mode_array,android.R.layout.simple_spinner_dropdown_item);
        txmodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txmode.setAdapter(txmodeAdapter);
        txmode.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                adMode = Utilidades.getADMode(getApplicationContext(),selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // NOP
            }
        });
        txmode.setSelection(adMode);
    }



    @Override
    public void onBackPressed()
    {
        boolean check_namespace = Utilidades.isValidHexString(namespace.getText().toString(), 10);
        boolean check_instance = Utilidades.isValidHexString(instance.getText().toString(), 6);
        if(check_namespace && check_instance){
            finish();
        }
        if(!check_namespace){
            namespace.setError("not 10-byte hex");
        }
        if(!check_instance){
            instance.setError("not 6-byte hex");
        }

    }
}
