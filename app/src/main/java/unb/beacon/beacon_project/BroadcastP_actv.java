package unb.beacon.beacon_project;

import android.app.Activity;
import android.os.Bundle;

public class BroadcastP_actv extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_p_actv);
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
