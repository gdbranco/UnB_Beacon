package unb.beacon.beacon_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

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
        /*double p1[] = {4.0,4.0};
        double p2[] = {9.0,7.0};
        double p3[] = {9.0,1.0};
        double d1 = 4,d2 = 3, d3 = 3.25;
        double[] pos;
        pos = Utilidades.trilaterar(p1,p2,p3,d1,d2,d3);
        texto.setText(String.format(Locale.US,"x: %f y: %f",pos[0],pos[1]));*/
    }
}
