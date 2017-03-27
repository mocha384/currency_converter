package com.rakeshgohel.currencyconverter.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface CurrencyService {
    @GET("/latest")
    Single<String> getDefaultCurrency();

    @GET("/latest")
    Single<String> getRatesByBase(@Query("base") String base);
}
