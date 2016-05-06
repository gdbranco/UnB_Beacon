package unb.beacon.beacon_project.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by pedro on 06/05/16.
 */
public class Utilidades extends Activity{


    public void showAlert(String title, String msg)
    {
        new AlertDialog.Builder(this)
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
