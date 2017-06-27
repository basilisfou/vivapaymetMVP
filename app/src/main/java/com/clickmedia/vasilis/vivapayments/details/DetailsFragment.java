package com.clickmedia.vasilis.vivapayments.details;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.clickmedia.vasilis.vivapayments.R;
import com.clickmedia.vasilis.vivapayments.adapters.CustomPageAdapter;
import com.clickmedia.vasilis.vivapayments.model.Product;
import com.clickmedia.vasilis.vivapayments.util.CommonUtils;
import com.clickmedia.vasilis.vivapayments.viewHelper.CirclePageIndicator;

import java.util.Calendar;

/**
 * Created by Vasilis Fouroulis on 24/6/2017. Main UI for the Details screen
 */

public class DetailsFragment extends Fragment implements DetailsContract.View{

    private final String TAG = getClass().getSimpleName() + "TAG";
    private RelativeLayout buyNow;
    private ProgressBar mProgressBar;
    private ViewPager mViewPager;
    private CustomPageAdapter mCustomPageAdapter;
    private CirclePageIndicator mIndicator;
    private TextView mProductTitleTextView, mProductDescriptionTextView, orderNumber;
    private Product product;
    private EditText dialogAmountEditText;
    private OnloadProductListener onloadProductListener;
    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }
    private DetailsContract.Presenter mPresenter;
    private Dialog dialog;
    private static OnSetDate onSetDateListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            onloadProductListener = (OnloadProductListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onCategoryClickedListener");
        }
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        Activity activity = null;
        if(c instanceof Activity){
            activity = (Activity) c;
        }

        try {
            onloadProductListener = (OnloadProductListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onCategoryClickedListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail_frag, container, false);

        buyNow                      = (RelativeLayout)      root.findViewById(R.id.btnBuyNow);
        mProgressBar                = (ProgressBar)         root.findViewById(R.id.pbLoadingDescription);
        mViewPager                  = (ViewPager)           root.findViewById(R.id.view_pager);
        mIndicator                  = (CirclePageIndicator) root.findViewById(R.id.indicator);
        mProductTitleTextView       = (TextView)            root.findViewById(R.id.tvproductTitle);
        mProductDescriptionTextView = (TextView)            root.findViewById(R.id.tvDescriptionItem);


        mPresenter.start();

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.buyProduct();
            }
        });

        initCardDialog();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            CommonUtils.showDialog(getActivity(),getString(R.string.loading_progress_dialog),false);
        } else {
            CommonUtils.hideDialog();
        }
    }

    @Override
    public void showTitle(String title) {
        mProductTitleTextView.setText(title);
        Log.d(TAG,"show Title : " + title);
    }

    @Override
    public void showDescription(String description) {
        mProductDescriptionTextView.setText(description);
        Log.d(TAG,"showDescription : " + description);

    }

    @Override
    public void showProduct(Product product) {
        this.product = product;
        Log.d(TAG,"product : " + product.getPrice());
    }

    @Override
    public void loadImages(String[] images) {
        mCustomPageAdapter = new CustomPageAdapter(getActivity(),mProgressBar,images);
        mViewPager.setAdapter(mCustomPageAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public boolean isActive() {
        Log.d(TAG,"isActive : " + isAdded());
        return isAdded();
    }

    @Override
    public void showPurchaseMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void showPrice(String price) {
        onloadProductListener.changeToolbarTitle(price);
    }

    @Override
    public void showDialogCard() {
        if(dialog != null){
            dialog.show();
        }
    }

    @Override
    public void setDialogValues(String orderId,String amount) {
        orderNumber.setText(orderId);
        dialogAmountEditText.setText(amount);

        Log.d(TAG,orderId + amount);
    }

    @Override
    public void setPresenter(DetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initCardDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText cvc , cardNumber, customerName, date;
        final Button doneBT, cancelBT;
        dialog.setContentView(R.layout.dialog_card);

        date         = (EditText) dialog.findViewById(R.id.dialog_date);
        customerName = (EditText) dialog.findViewById(R.id.dialog_name);
        cvc          = (EditText) dialog.findViewById(R.id.dialog_cvc);
        cardNumber   = (EditText) dialog.findViewById(R.id.dialog_number_of_card);
        doneBT       = (Button)   dialog.findViewById(R.id.ok_bt);
        cancelBT     = (Button)   dialog.findViewById(R.id.cancel_bt);
        orderNumber  = (TextView) dialog.findViewById(R.id.dialog_order);
        dialogAmountEditText = (EditText)dialog.findViewById(R.id.dialog_amount);

        cardNumber.setEnabled(false);
        cvc.setEnabled(false);
        dialogAmountEditText.setEnabled(false);

        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        onSetDateListener = new OnSetDate() {
            @Override
            public void setDate(String newDate) {
                date.setText(newDate);
            }
        };


        cvc.setText("111");
        cardNumber.setText("4111 1111 1111 1111");

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cvc.getText().toString().length() == 3 || cvc.getText().toString().length() == 3
                        && cardNumber.getText().toString().length() == 18
                        && customerName.getText().toString().length() > 8
                        && date.getText().toString().length() >= 6){
                    Log.d(TAG,"tokenizeCard" + " " + customerName.getText().toString() + " " + cardNumber.getText().toString()+ " " +cvc.getText().toString()+ " " +date.getText().toString());
                    mPresenter.tokenizeCard(customerName.getText().toString(),cardNumber.getText().toString(),cvc.getText().toString(),date.getText().toString());
                } else {
                    Log.d(TAG,"ERROR tokenizeCard");
                    Toast.makeText(getActivity(),getString(R.string.fill_in_all),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Interface that listens to the clicks of the categories ,
     * Implemented by Main Activity , used by this Fragment
     */
    public interface OnloadProductListener {
        void changeToolbarTitle(String title);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.d("DetailsFragment","year " + year  + " month " + month + " day " + day);
            int fixedMonth = month+1;
            String monthString = fixedMonth < 10 ? "0"+fixedMonth: ""+fixedMonth;
            onSetDateListener.setDate(""+year+"-"+(monthString)+"-"+day);
        }
    }

    public interface OnSetDate {
        void setDate(String date);
    }
}
