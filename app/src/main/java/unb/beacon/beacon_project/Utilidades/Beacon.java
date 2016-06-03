package unb.beacon.beacon_project.Utilidades;

import java.util.Locale;

/**
 * Created by dbranco on 03/06/2016.
 */
public class Beacon {
    public String name;
    public String id;
    public int rssi;
    public long lastTimeStamp;
    public int power;
    public int cont;
    public double distance;
    public double mdistancia;
    public int mrssi;


    public Beacon(String _name, String _id, int _rssi, int _power, long _ts,double _distance)
    {
        this.name = _name;
        this.id = _id;
        this.rssi = _rssi;
        this.power = _power;
        this.lastTimeStamp = _ts;
        this.cont = 1;
        this.distance = _distance;
        this.mrssi = rssi;
        this.mdistancia = distance;
    }

    public void update(int _rssi, int _power, long _ts, double _distance)
    {
        this.rssi += _rssi;
        this.power = _power;
        this.lastTimeStamp = _ts;
        this.distance += _distance;
        this.cont++;
        if(cont>=10)
        {
            this.mrssi = rssi/cont;
            this.mdistancia = distance/cont;
            this.distance = _distance;
            this.rssi = _rssi;
            this.cont = 1;
        }

    }

    @Override
    public String toString()
    {
        return ("CONT: " + cont + "\nNAMESPACE: " + name + "\nID: " + id +"\nTXPOWER: " + power + "\nRSSI: " + mrssi + "\nDISTANCE: " + mdistancia);
    }

    public static String getInstanceNSpace(byte[] data)
    {
        StringBuilder sb = new StringBuilder();
        int pl = 18 - 6;
        int offset = pl - 10;
        for(int i=offset;i<pl;i++)
        {
            sb.append(Integer.toHexString(data[i] & 0xFF));
        }
        return sb.toString();
    }

    public static String getInstanceId(byte[] data)
    {
        StringBuilder sb = new StringBuilder();
        int pl = 18;
        int offset = pl - 6;
        for(int i=offset;i<pl;i++)
        {
            sb.append(Integer.toHexString(data[i] & 0xFF));
        }
        return sb.toString();
    }
}
