package com.rakeshgohel.currencyconverter;

import android.app.Application;

import com.rakeshgohel.currencyconverter.data.Config;
import com.rakeshgohel.currencyconverter.data.DaoSessionManager;

/**
 * Created by rgohel on 2017-03-25.
 */

public class CurrencyConverterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DaoSessionManager.init(this);
        Config.init(this);
    }
}
