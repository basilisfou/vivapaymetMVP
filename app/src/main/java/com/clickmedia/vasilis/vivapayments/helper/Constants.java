package com.clickmedia.vasilis.vivapayments.helper;

/**
 * Created by Vasilis Fouroulis on 25/6/2017.
 */

public class Constants {

    public static final String MERCHANT_TRNS = "Vasilis Fouroulis Store";
    //users credentials
    public static final String MERCHAND_ID = "f80b94ff-2d28-4eb6-a69c-a3ed021af336";
    public static final String API_KEY     = "nKoQ+;";
    public static final String PUBLIC_KEY  = "rbURZitxSNrAJI122I2F5JFBMaUV23lACn7K1KIAUOU=";
    public static final String SOURCE_CODE = "2577";

    //Endpoints
    public static final String BASE_URL    = "http://demo.vivapayments.com/"; // DEMO E
    //private static final String BASE_URL    = "http://demo.vivapayments.com";
    public static final String CREATE_PAYMENT_ORDER = BASE_URL + "api/Orders";
    public static final String CARD_TOKENIZATION    = BASE_URL + "api/cards?key=" + PUBLIC_KEY;
    public static final String PAYMENT_EXECUTION    = BASE_URL + "api/Transactions";


}
