package com.clickmedia.vasilis.vivapayments.webServices;

import android.os.AsyncTask;
import android.util.Log;

import com.clickmedia.vasilis.vivapayments.helper.Constants;
import com.clickmedia.vasilis.vivapayments.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;



/**
 * Created by Vasilis Fouroulis on 25/06/2017. VivaWallet API Create Order
 */

public class HttpCreaterOrder extends AsyncTask<String,Integer,Object> {

    private final String TAG = getClass().getSimpleName();
    private int responseCode = 0;
    private MyAsyncTaskListener mListener;
    private String email, phone, FullName,  CustomerTrns;
    private int MaxInstallment;
    private long Amount;

    public HttpCreaterOrder(MyAsyncTaskListener listener, String email, String phone, String FullName,int MaxInstallment,long Amount,String CustomerTrns){
        this.mListener = listener;
        this.email = email;
        this.phone = phone;
        this.FullName = FullName;
        this.MaxInstallment = MaxInstallment;
        this.CustomerTrns = CustomerTrns;
        this.Amount = Amount;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onPreExecutelistener();
    }

    @Override
    protected Object doInBackground(String... params) {
        URL url;
        String response = "";

        try {

            url = new URL(Constants.CREATE_PAYMENT_ORDER);
            Log.d(TAG,"****************************************************************************************************************************************************************************************");
            Log.d(TAG,"requestURL: " + Constants.CREATE_PAYMENT_ORDER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic  " + CommonUtils.encodeBase64(Constants.MERCHAND_ID + ":" + Constants.API_KEY));
            connection.setDoInput(true);
            connection.setDoOutput(true);

            String body = setJSONBody();
            Log.d(TAG,Constants.CREATE_PAYMENT_ORDER + " BODY: " + body);
            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStream.write(body.getBytes());
            outputStream.flush();

            responseCode = connection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK){
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null){response+=line;}
                Log.d(TAG,Constants.CREATE_PAYMENT_ORDER + " RESPONSE: " + response);
            } else {
                response = "";
                Log.w(TAG,Constants.CREATE_PAYMENT_ORDER + " ERROR : " + responseCode);
            }
        } catch (Exception e ){
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mListener.onPostExecutelistener(o.toString(),responseCode);
    }

    public interface MyAsyncTaskListener {
        void onPreExecutelistener();
        void onPostExecutelistener(String result, int responseCode);
    }

    private String setJSONBody(){
        Log.d(TAG,"Amount:" + Amount);
        JSONObject body = new JSONObject();

        try {
            body.put("Tags","");
            body.put("Email",email);
            body.put("Phone",phone);
            body.put("FullName",FullName);
            body.put("PaymentTimeOut",86400);
            body.put("RequestLang","en-US");
            body.put("MaxInstallments",MaxInstallment);
            body.put("AllowRecurring",true);
            body.put("IsPreAuth",true);
            body.put("Amount",Amount);
            body.put("MerchantTrns",Constants.MERCHANT_TRNS);
            body.put("CustomerTrns",CustomerTrns);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body.toString();
    }
}
