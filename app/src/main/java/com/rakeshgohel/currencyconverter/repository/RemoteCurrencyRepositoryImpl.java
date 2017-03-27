package com.rakeshgohel.currencyconverter.repository;

import com.rakeshgohel.currencyconverter.data.DaoSessionManager;
import com.rakeshgohel.currencyconverter.models.Currency;
import com.rakeshgohel.currencyconverter.models.CurrencyDao;
import com.rakeshgohel.currencyconverter.service.RestApiClient;

import org.greenrobot.greendao.query.Query;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by rgohel on 2017-03-25.
 */

public class RemoteCurrencyRepositoryImpl implements RemoteCurrencyRepository {

    @Override
    public Single<String> getCurrencyJson() {
        return RestApiClient.Instance().getCurrencyApiService().getDefaultCurrency();
    }

}
