package com.clickmedia.vasilis.vivapayments.details;

import com.clickmedia.vasilis.vivapayments.model.Product;
import com.clickmedia.vasilis.vivapayments.util.BasePresenter;
import com.clickmedia.vasilis.vivapayments.util.BaseView;

import java.util.List;

/**
 * Created by Vasilis Fouroulis on 24/6/2017.
 */

public interface DetailsContract {

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showTitle(String title);

        void showDescription(String description);

        void showProduct(Product product);

        void loadImages(String[] images);

        boolean isActive();

        void showPurchaseMessage(String message);

        void showPrice(String price);

        void showDialogCard();

        void setDialogValues(String orderId, String Amount);

    }
    //Actions
    interface Presenter extends BasePresenter {

        void buyProduct();

        void tokenizeCard(String name, String numberCard, String cvc, String date );


    }
}
