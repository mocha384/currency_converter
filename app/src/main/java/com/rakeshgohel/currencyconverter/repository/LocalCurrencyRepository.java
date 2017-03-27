package com.rakeshgohel.currencyconverter.repository;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface LocalCurrencyRepository {
    Single<List<Currency>> getCurrencies();
    void setCurrencies(String jsonData);
}
