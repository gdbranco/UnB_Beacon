package unb.beacon.beacon_project.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

/**
 * Created by pedro on 06/05/16.
 */
public class Utilidades extends Activity{

    public static final String P_NAMESPACE = "namespace";
    public static final String P_INSTANCE = "instance";
    public static final String SHARED_PREFS_NAME = "beacon-uid-prefs";

    public static void showAlert(String title, String msg, Activity a)
    {
        new AlertDialog.Builder(a)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                    }
                }).show();
    }
}
