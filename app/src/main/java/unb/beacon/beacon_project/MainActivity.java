package unb.beacon.beacon_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import unb.beacon.beacon_project.Utilidades.Utilidades;


public class MainActivity extends AppCompatActivity{
    TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilidades.SharedPreferencesManager.getInstance(getApplicationContext());
        init();
    }

    public void init(){
        Interface();
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
}
