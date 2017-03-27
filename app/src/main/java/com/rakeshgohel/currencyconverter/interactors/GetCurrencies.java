package com.rakeshgohel.currencyconverter.interactors;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface GetCurrencies<T> {
    void execute(Callback<T> callback);
    void stop();
}
