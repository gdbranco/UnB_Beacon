package unb.beacon.beacon_project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import unb.beacon.beacon_project.Utilidades.Utilidades;

public class BroadcastP_actv extends Activity {
    private EditText namespace;
    private EditText instance;

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
        }
    }

    private void Interface()
    {
        namespace = (EditText) findViewById(R.id.namespace_string);
        namespace.setText(Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_NAMESPACE));
        instance = (EditText) findViewById(R.id.instance_string);
        instance.setText(Utilidades.SharedPreferencesManager.getInstance().getString(Utilidades.P_INSTANCE));
    }

    @Override
    public void onBackPressed()
    {
        boolean check_namespace = isValid(namespace.getText().toString(), 10);
        boolean check_instance = isValid(instance.getText().toString(), 6);
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

    public boolean isValid(String s, int len) {
        return !(s == null || s.isEmpty()) && s.length() == len*2 && s.matches("[0-9A-F]+");
    }
}
