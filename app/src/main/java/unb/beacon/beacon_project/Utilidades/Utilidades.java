package unb.beacon.beacon_project.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.ParcelUuid;

import java.io.CharConversionException;
import java.util.Random;

import unb.beacon.beacon_project.R;

/**
 * Created by pedro on 06/05/16.
 */
public class Utilidades extends Activity{


    public static final ParcelUuid SERVICE_UUID = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");
    public static final byte FRAME_TYPE_UID = 0x00;
    public static final String P_NAMESPACE = "namespace";
    public static final String P_INSTANCE = "instance";
    public static final String P_TXPOWER = "tx_power_level";
    public static final String P_TXMODE = "tx_advertise_mode";


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

    public static String getPowerLevel(Context context,int powerlevel)
    {
        String s = "";
        switch(powerlevel)
        {
            case AdvertiseSettings.ADVERTISE_TX_POWER_HIGH:
                s = context.getString(R.string.tx_power_high);
                break;
            case AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM:
                s = context.getString(R.string.tx_power_medium);
                break;
            case AdvertiseSettings.ADVERTISE_TX_POWER_LOW:
                s = context.getString(R.string.tx_power_low);
                break;
            case AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW:
                s = context.getString(R.string.tx_power_ultra_low);
                break;
        }
        return s;
    }

    public static int getPowerLevel(Context context,String powerlevel)
    {
        int s = 0;
        if (powerlevel.equals(context.getString(R.string.tx_power_high))) {
            s = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;
        } else if (powerlevel.equals(context.getString(R.string.tx_power_medium))) {
            s = AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM;
        } else if (powerlevel.equals(context.getString(R.string.tx_power_low))) {
            s = AdvertiseSettings.ADVERTISE_TX_POWER_LOW;
        } else if (powerlevel.equals(context.getString(R.string.tx_power_ultra_low))) {
            s = AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW;
        }
        return s;
    }

    public static String getADMode(Context context, int admode)
    {
        String s = "";
        switch(admode)
        {
            case AdvertiseSettings.ADVERTISE_MODE_LOW_POWER:
                s = context.getString(R.string.tx_mode_low_power);
                break;
            case AdvertiseSettings.ADVERTISE_MODE_BALANCED:
                s = context.getString(R.string.tx_mode_balanced);
                break;
            case AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY:
                s = context.getString(R.string.tx_mode_low_latency);
                break;
        }
        return s;
    }

    public static int getADMode(Context context,String admode)
    {
        int s = 0;
        if (admode.equals(context.getString(R.string.tx_mode_low_latency))) {
            s = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;
        } else if (admode.equals(context.getString(R.string.tx_mode_balanced))) {
            s = AdvertiseSettings.ADVERTISE_MODE_BALANCED;
        } else if (admode.equals(context.getString(R.string.tx_mode_low_power))) {
            s = AdvertiseSettings.ADVERTISE_MODE_LOW_POWER;
        }
        return s;
    }

    // Converts the current Tx power level value to the byte value for that power
    // in dBm at 0 meters.
    //
    // Note that this will vary by device and the values are only roughly accurate.
    // The measurements were taken with a Nexus 6.
    public static byte txPowerLevelToByteValue(int powerlvl) {
        switch (powerlvl) {
            case AdvertiseSettings.ADVERTISE_TX_POWER_HIGH:
                return (byte) -16;
            case AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM:
                return (byte) -26;
            case AdvertiseSettings.ADVERTISE_TX_POWER_LOW:
                return (byte) -35;
            default:
                return (byte) -59;
        }
    }

    public static byte[] toByteArray(String s)
    {
        int len = s.length();
        byte[] bytes = new byte[len/2];
        for(int i=0;i<len;i+=2)
        {
            bytes[i/2] = (byte) ((Character.digit(s.charAt(i),16) << 4) + Character.digit(s.charAt(i+1),16));
        }
        return bytes;
    }

    public static String randomHexString(int len)
    {
        byte[] buf = new byte[len];
        new Random().nextBytes(buf);
        StringBuilder string = new StringBuilder();
        for(int i=0;i<len;i++)
        {
            string.append(String.format("%02X",buf[i]));
        }
        return string.toString();
    }

    public static boolean isValidHexString(String s, int len) {
        return !(s == null || s.isEmpty()) && s.length() == len*2 && s.matches("[0-9A-F]+");
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

        public String getString(String key, String defaultValue)
        {
            return mPref.getString(key, defaultValue);
        }

        public String getString(String key)
        {
            return mPref.getString(key,null);
        }

        public int getInt(String key, int defaultValue)
        {
            return mPref.getInt(key,defaultValue);
        }

        public int getInt(String key)
        {
            return mPref.getInt(key,0);
        }

        public void putString(String key, String value)
        {
            doEdit();
            mEditor.putString(key,value);
            doCommit();

        }

        public void putInt(String key, int value)
        {
            doEdit();
            mEditor.putInt(key,value);
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
