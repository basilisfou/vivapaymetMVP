package com.clickmedia.vasilis.vivapayments.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vasilis Fouroulis on 25/6/2017.
 */

public class CommonUtils {

    private static ProgressDialog waitingDialog;
    private static final String TAG = "CommonUtils";

    public static void showDialog(Activity ctx, String message, boolean cancelable) {
        if (waitingDialog != null) {
            hideDialog();
            waitingDialog = null;
        }
        if(ctx != null) {
            waitingDialog = new ProgressDialog(ctx);
            waitingDialog.setMessage(message);
            waitingDialog.setCancelable(cancelable);
            try {
                waitingDialog.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void hideDialog() {
        if (waitingDialog != null && waitingDialog.isShowing())
            waitingDialog.dismiss();
    }

    public static String encodeBase64(String credetials){
        byte[] data = new byte[0];

        try {
            data = credetials.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static long setPrice(String price){
        String[] strings = price.split(" ");

        float priceFloat = Float.parseFloat(strings[0].replace("€","")) * 100;
        long pricelong = (long) priceFloat;
        Log.d(TAG,"priceFloat:" + priceFloat);
        return pricelong;

    }

    public static boolean isNetworkAvailable(Context ctx){
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(
                    Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                //Log.d(TAG,"CommonUtils , cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)");
                return true;
            }

            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {
                //Log.d(TAG,"CommonUtils, cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)");
                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                //Log.d(TAG,"CommonUtils, cm.getActiveNetworkInfo()");

                return true;
            }
            //Log.d(TAG,"CommonUtils, no internet");
            return false;
        } catch (Exception e) {
//			Log.e(TAG,e.toString());
            return false;
        }
    }

    public static String fixDate(String input){
        Date parsedDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            parsedDate = formatter.parse(input);
            return ""+parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
