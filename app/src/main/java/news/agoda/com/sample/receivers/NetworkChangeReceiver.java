package news.agoda.com.sample.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import news.agoda.com.sample.router.ConnectivityListener;

/**
 * Created by Sundararaghavan on 11/27/2018.
 */
public class NetworkChangeReceiver extends BroadcastReceiver
{
    private static final String TAG = "NetworkChangeReceiver";
    private ConnectivityListener connectivityListener;

    public NetworkChangeReceiver(ConnectivityListener connectivityListener) {

        this.connectivityListener = connectivityListener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                Log.e(TAG, "Online Connect Intenet ");
                connectivityListener.onNetworkStateChanged(true);
            } else {
                Log.e(TAG, "Connectivity Failure !!! ");
                connectivityListener.onNetworkStateChanged(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}