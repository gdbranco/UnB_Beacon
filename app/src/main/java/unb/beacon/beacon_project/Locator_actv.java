package unb.beacon.beacon_project;

import android.app.Activity;
import android.os.Bundle;

public class Locator_actv extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
    }

    public void onBackPressed()
    {
        finish();
    }
}
