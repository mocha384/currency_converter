package com.rakeshgohel.currencyconverter.service;

import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by rgohel on 2017-03-25.
 */

public class RestApiClient {
    private String SERVICE_ENDPOINT = "http://api.fixer.io/";
    private CurrencyService mCurrencyApiService;
    private static RestApiClient mRestClient;

    private RestApiClient() {
        mCurrencyApiService = ServiceFactory.createRetrofitService(CurrencyService.class,  SERVICE_ENDPOINT, ScalarsConverterFactory.create());
    }

    public static synchronized RestApiClient Instance() {
        if (mRestClient == null) {
            mRestClient = new RestApiClient();
        }
        return mRestClient;
    }

    public CurrencyService getCurrencyApiService() {
        return mCurrencyApiService;
    }

}
