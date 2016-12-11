package vn.brine.haileader.expolatorysearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by haileader on 27/10/16.
 */
public class NetworkHelper {
    private static final String TAG=NetworkHelper.class.getSimpleName();
    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"No Network connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," Network connection available...");
                return true;
            }
            else
            {
                Log.d(TAG,"no Network connection");
                return false;
            }

        }
    }
}
