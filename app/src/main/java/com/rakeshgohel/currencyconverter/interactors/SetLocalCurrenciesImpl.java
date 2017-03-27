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

public class SetLocalCurrenciesImpl implements SetCurrencies {
    private static final String TAG = SetLocalCurrenciesImpl.class.getSimpleName();
    private LocalCurrencyRepository mCurrencyRepository;

    public SetLocalCurrenciesImpl() {
        this(new LocalCurrencyRepositoryImpl());
    }

    SetLocalCurrenciesImpl(LocalCurrencyRepository currencyRepository) {
        mCurrencyRepository = currencyRepository;
    }

    @Override
    public void updateDataLocally(String jsonData) {
        mCurrencyRepository.setCurrencies(jsonData);

    }
}
