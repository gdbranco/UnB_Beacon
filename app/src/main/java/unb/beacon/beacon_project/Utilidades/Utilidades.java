package unb.beacon.beacon_project.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by pedro on 06/05/16.
 */
public class Utilidades extends Activity{

    public static final String P_NAMESPACE = "namespace";
    public static final String P_INSTANCE = "instance";


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
    public static class SharedPreferencesManager {

        public static final String SHARED_PREFS_NAME = "beacon-uid-prefs";
        private static SharedPreferencesManager instance = null;
        private SharedPreferences mPref;
        private SharedPreferences.Editor mEditor;


        private SharedPreferencesManager(Context context) {
            mPref = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        }

        public static SharedPreferencesManager getInstance(Context context)
        {
            if(instance==null)
            {
                instance = new SharedPreferencesManager(context);
            }
            return instance;
        }

        public static SharedPreferencesManager getInstance()
        {
            if(instance!=null)
            {
                return instance;
            }
            throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
        }

        private static SharedPreferences getSharedPreferences(Context context) {
            return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        }

        public String getString(String key, String defaultValue) {
            return mPref.getString(key, defaultValue);
        }

        public String getString(String key)
        {
            return mPref.getString(key,null);
        }

        public void putString(String key, String value)
        {
            doEdit();
            mEditor.putString(key,value);
            doCommit();

        }

        public void clear()
        {
            doEdit();
            mEditor.clear();
            doCommit();
        }

       private void doEdit()
       {
           if(mEditor == null)
           {
               mEditor = mPref.edit();
           }
       }

        private void doCommit()
        {
            if(mEditor != null)
            {
                mEditor.commit();
                mEditor = null;
            }
        }
    }
}
