package com.rakeshgohel.currencyconverter.interactors;

import android.util.Log;

import com.rakeshgohel.currencyconverter.repository.RemoteCurrencyRepository;
import com.rakeshgohel.currencyconverter.repository.RemoteCurrencyRepositoryImpl;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rgohel on 2017-03-26.
 */

public class GetRemoteCurrenciesImpl implements GetCurrencies<String> {
    private static final String TAG = GetRemoteCurrenciesImpl.class.getSimpleName();

    private final RemoteCurrencyRepository mRemoteCurrencyRespository;
    private Disposable mDisposableCurrencyJson;

    public GetRemoteCurrenciesImpl() {
        this(new RemoteCurrencyRepositoryImpl());
    }

    GetRemoteCurrenciesImpl(RemoteCurrencyRepository remoteCurrencyRepository) {
        mRemoteCurrencyRespository = remoteCurrencyRepository;
    }

    @Override
    public void execute(Callback<String> callback) {
        mRemoteCurrencyRespository
                .getCurrencyJson()
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposableCurrencyJson = d;
                    }

                    @Override
                    public void onSuccess(String json) {
                        callback.onSuccess(json);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onFailure");
                        callback.onFailure(e);
                    }
                });
    }

    @Override
    public void stop() {
        if (mDisposableCurrencyJson != null && !mDisposableCurrencyJson.isDisposed()) {
            mDisposableCurrencyJson.dispose();
            mDisposableCurrencyJson = null;
        }
    }
}
