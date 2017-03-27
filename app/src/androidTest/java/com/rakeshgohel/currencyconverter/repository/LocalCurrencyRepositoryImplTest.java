package com.rakeshgohel.currencyconverter.repository;

import android.support.test.InstrumentationRegistry;

import com.rakeshgohel.currencyconverter.data.DaoSessionManager;
import com.rakeshgohel.currencyconverter.models.Currency;

import org.junit.Test;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by rgohel on 2017-03-26.
 */
public class LocalCurrencyRepositoryImplTest {
    @Test
    public void setCurrencies() throws Exception {
        String jsonData = "{" +
                "  \"base\": \"USD\",\n" +
                "  \"date\": \"2017-03-26\",\n" +
                "  \"rates\": {\n" +
                "    \"AUD\": 1.3097,\n" +
                "    \"BGN\": 1.7961 } }";

        String[] currencies = new String[] {"EUR","AUD", "BGN"};

        DaoSessionManager.init(InstrumentationRegistry.getContext());
        LocalCurrencyRepositoryImpl localCurrencyRepository = new LocalCurrencyRepositoryImpl();
        localCurrencyRepository.setCurrencies(jsonData);
        localCurrencyRepository.getCurrencies().observeOn(Schedulers.io()).subscribe(new SingleObserver<List<Currency>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Currency> list) {
                for (Currency currency : list) {
                    boolean found = false;
                    for (int i=0; i< currencies.length; ++i) {
                        if (currency.getName().compareTo(currencies[i]) == 0) {
                            found = true;
                            break;
                        }
                    }
                    assert (found);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}