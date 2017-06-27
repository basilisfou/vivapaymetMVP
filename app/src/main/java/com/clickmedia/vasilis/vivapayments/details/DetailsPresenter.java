package com.clickmedia.vasilis.vivapayments.details;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.clickmedia.vasilis.vivapayments.model.Product;
import com.clickmedia.vasilis.vivapayments.model.User;
import com.clickmedia.vasilis.vivapayments.util.CommonUtils;
import com.clickmedia.vasilis.vivapayments.webServices.HttpCardTokenization;
import com.clickmedia.vasilis.vivapayments.webServices.HttpCreaterOrder;
import com.clickmedia.vasilis.vivapayments.webServices.HttpPaymentExecution;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Vasilis Fouroulis on 24/6/2017.
 * Listens to user actions from the UI ({@link DetailsFragment}), retrieves the data and updates
 * the UI as required.
 */

public class DetailsPresenter  implements DetailsContract.Presenter  {
    private static final String TAG = "DetailsPresenterTAG";

    private final DetailsContract.View mDetailsView;
    private final Product mProduct;
    private final User user;
    private long orderCode = 0;

    @Override
    public void start() {
        loadProduct();
    }

    public DetailsPresenter(@NonNull Product product,@NonNull DetailsContract.View statisticsView, User user) {

        this.mProduct = product; //todo this should be a repository in real life

        mDetailsView = statisticsView;
        mDetailsView.setPresenter(this);
        this.user = user;

        Log.d(TAG,"New Presenter");

    }

    private void loadProduct() {
        //todo simulate loading product, delete handler in case of a repository call , in preExecute we starting loading, in onLoadCompleted we stop loading

        mDetailsView.setProgressIndicator(true);

        Log.d(TAG,"Load Product");

        new Handler().postDelayed(new Runnable(){
            public void run() {

                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }

                mDetailsView.setProgressIndicator(false);
                mDetailsView.showTitle(mProduct.getTitle());
                mDetailsView.showDescription(mProduct.getDescription());
                mDetailsView.showProduct(mProduct);
                mDetailsView.loadImages(mProduct.getProductImage());
                mDetailsView.showPrice(mProduct.getPrice());
                Log.d(TAG,"Product loaded");


                //show dialog card in order to fulfil the payment
            }}, 4000);
    }

    @Override
    public void buyProduct() {
        new HttpCreaterOrder(new HttpCreaterOrder.MyAsyncTaskListener() {
            @Override
            public void onPreExecutelistener() {
                if (!mDetailsView.isActive()) {
                    return;
                }

                mDetailsView.setProgressIndicator(true);
            }

            @Override
            public void onPostExecutelistener(String result, int responseCode) {
                // The view may not be able to handle UI updates anymore
                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    boolean success = jsonObject.getBoolean("Success");



                    if (errorCode == 0 && success) {
                        orderCode = jsonObject.getLong("OrderCode");
                        mDetailsView.showDialogCard();
                        mDetailsView.setDialogValues(""+orderCode,mProduct.getPrice());
                    } else {
                        String errorText = jsonObject.getString("ErrorText");
                        mDetailsView.showPurchaseMessage(errorText);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mDetailsView.setProgressIndicator(false);

            }
        }, user.getEmail(), user.getPhone(), user.getFullname(), mProduct.getMaxInstallment(), CommonUtils.setPrice(mProduct.getPrice()), mProduct.getTitle()).execute();
    }

    @Override
    public void tokenizeCard(String name, String numberCard, String cvc, String date) {

        new HttpCardTokenization(new HttpCardTokenization.MyAsyncTaskListener() {


            @Override
            public void onPreExecutelistener() {
                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }
                mDetailsView.setProgressIndicator(true);
            }

            @Override
            public void onPostExecutelistener(String result, int responseCode) {

                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }
                try {
                    JSONObject responce = new JSONObject(result);

                    String tokenCard = responce.getString("Token");
                    paymentExecution(orderCode,tokenCard);
                } catch (JSONException e) {
                    e.printStackTrace();

                    mDetailsView.setProgressIndicator(false);
                }

            }

            @Override
            public void onError(String message) {
                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }

                Log.e(TAG,"ERROR " + message);

                mDetailsView.showPurchaseMessage(message);
            }

        },numberCard.replace(" ",""),Integer.parseInt(cvc),date,name).execute();
    }


    private void paymentExecution(long OrderCode, String Token) {

        new HttpPaymentExecution(new HttpPaymentExecution.MyAsyncTaskListener() {
            @Override
            public void onPreExecutelistener() {

            }

            @Override
            public void onPostExecutelistener(String result, int responseCode) {
                Log.d(TAG,result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");

                    if (errorCode == 0 ) {
                        String StatusId = jsonObject.getString("StatusId");
                        String TransactionId = jsonObject.getString("TransactionId");

                        mDetailsView.showPurchaseMessage("Purchase has been made with statusId : " + StatusId + " and TransactionId " + TransactionId);

                    } else {
                        String errorText = jsonObject.getString("ErrorText");
                        mDetailsView.showPurchaseMessage(errorText);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mDetailsView.setProgressIndicator(false);
            }

            @Override
            public void onError(String message) {
                // The view may not be able to handle UI updates anymore
                if (!mDetailsView.isActive()) {
                    return;
                }

                Log.e(TAG,"ERROR " + message);

                mDetailsView.showPurchaseMessage(message);
            }
        },OrderCode,Token).execute();
    }
}
