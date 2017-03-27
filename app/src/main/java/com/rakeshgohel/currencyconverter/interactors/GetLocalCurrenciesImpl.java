package com.rakeshgohel.currencyconverter.interactors;

import android.util.Log;

import com.rakeshgohel.currencyconverter.models.Currency;
import com.rakeshgohel.currencyconverter.repository.LocalCurrencyRepository;
import com.rakeshgohel.currencyconverter.repository.LocalCurrencyRepositoryImpl;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rgohel on 2017-03-25.
 */

public class GetLocalCurrenciesImpl implements GetCurrencies<List<Currency>> {
    private static final String TAG = GetLocalCurrenciesImpl.class.getSimpleName();
    private LocalCurrencyRepository mCurrencyRepository;
    private Single<List<Currency>> mSingleCurrencyList;
    private Disposable mDisposableCurrencyList;

    public GetLocalCurrenciesImpl() {
        this(new LocalCurrencyRepositoryImpl());
    }

    GetLocalCurrenciesImpl(LocalCurrencyRepository currencyRepository) {
        mCurrencyRepository = currencyRepository;
        mSingleCurrencyList = mCurrencyRepository.getCurrencies();
    }

    @Override
    public void execute(Callback<List<Currency>> callback) {
        mSingleCurrencyList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposableCurrencyList = d;
                    }

                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        callback.onSuccess(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        callback.onFailure(e);
                    }
                });
    }

    @Override
    public void stop() {
        if (mDisposableCurrencyList != null && !mDisposableCurrencyList.isDisposed()) {
            mDisposableCurrencyList.dispose();
            mDisposableCurrencyList = null;
        }
    }
}
