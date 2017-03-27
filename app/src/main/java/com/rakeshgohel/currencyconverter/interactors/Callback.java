package com.rakeshgohel.currencyconverter.interactors;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

/**
 * Created by rgohel on 2017-03-26.
 */

public interface Callback<T> {
    void onSuccess(T arg);
    void onFailure(Throwable error);
}
