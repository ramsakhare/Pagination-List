package com.SampleApp.Sample.helper;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RequiredFunction {

    public boolean isConnected(Activity a) {
        NetworkInfo networkInfo;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) a.getSystemService(Activity.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();

            //Toast.makeText(a, ""+networkInfo, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            networkInfo = null;
        }
        if (networkInfo != null && networkInfo.isConnected())
            return true;

        else
            return false;

    }

}
