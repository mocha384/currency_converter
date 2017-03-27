package com.rakeshgohel.currencyconverter.repository;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface RemoteCurrencyRepository {
    Single<String> getCurrencyJson();
}
