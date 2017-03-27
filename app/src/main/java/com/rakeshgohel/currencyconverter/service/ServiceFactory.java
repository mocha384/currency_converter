package com.rakeshgohel.currencyconverter.service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by rgohel on 2017-01-20.
 */

public class ServiceFactory {
    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint, Converter.Factory factory) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .baseUrl(endPoint)
                .build()
                .create(clazz);
    }
}
