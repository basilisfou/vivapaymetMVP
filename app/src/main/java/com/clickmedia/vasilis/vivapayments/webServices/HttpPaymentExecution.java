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

public class HttpPaymentExecution extends AsyncTask<String,Integer,Object> {

    private final String TAG = getClass().getSimpleName();
    private int responseCode = 0;
    private MyAsyncTaskListener mListener;
    private String  Token;
    private long OrderCode;


    public HttpPaymentExecution(MyAsyncTaskListener listener, long OrderCode, String Token){
        this.mListener = listener;
        this.OrderCode = OrderCode;
        this.Token = Token;
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
            String endpoint = Constants.PAYMENT_EXECUTION;
            url = new URL(endpoint);
            Log.d(TAG,"****************************************************************************************************************************************************************************************");
            Log.d(TAG,"requestURL: " + endpoint);
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
            Log.d(TAG,endpoint + " BODY: " + body);
            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStream.write(body.getBytes());
            outputStream.flush();

            responseCode = connection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK){
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null){response+=line;}
                Log.d(TAG,endpoint + " RESPONSE: " + response);
            } else {
                mListener.onError(connection.getResponseMessage());
                Log.w(TAG,endpoint + " ERROR : " + responseCode);
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
        void onError(String message);
    }

    private String setJSONBody(){

        JSONObject body = new JSONObject();

        try {
            body.put("OrderCode",OrderCode);
            body.put("SourceCode",Constants.SOURCE_CODE);

            JSONObject creditCard =  new JSONObject();
            creditCard.put("Token",Token);

            body.put("CreditCard",creditCard);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body.toString();
    }
}
