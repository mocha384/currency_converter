package com.rakeshgohel.currencyconverter.presenters;

import android.util.Log;

import com.rakeshgohel.currencyconverter.data.Config;
import com.rakeshgohel.currencyconverter.events.EventDataUpdated;
import com.rakeshgohel.currencyconverter.events.EventMarketRatesUpdateRequired;
import com.rakeshgohel.currencyconverter.interactors.Callback;
import com.rakeshgohel.currencyconverter.interactors.GetCurrencies;
import com.rakeshgohel.currencyconverter.interactors.GetLocalCurrenciesImpl;
import com.rakeshgohel.currencyconverter.interactors.GetRemoteCurrenciesImpl;
import com.rakeshgohel.currencyconverter.interactors.SetCurrencies;
import com.rakeshgohel.currencyconverter.interactors.SetLocalCurrenciesImpl;
import com.rakeshgohel.currencyconverter.models.Currency;
import com.rakeshgohel.currencyconverter.ui.views.CurrencyConverterView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by rgohel on 2017-03-25.
 */

public class CurrencyActivityPresenterImpl implements CurrencyActivityPresenter {
    private static final String TAG = CurrencyActivityPresenterImpl.class.getSimpleName();

    private CurrencyConverterView   mCurrencyConverterView;
    private final GetCurrencies     mGetLocalCurrencies;
    private final GetCurrencies     mGetRemoteCurrencies;
    private final SetCurrencies     mSetLocalCurrencies;

    private List<Currency>          mCurrencyList;

    public CurrencyActivityPresenterImpl(CurrencyConverterView view) {
        this(view, new GetLocalCurrenciesImpl(), new GetRemoteCurrenciesImpl(), new SetLocalCurrenciesImpl());
    }

    CurrencyActivityPresenterImpl(CurrencyConverterView currencyConverterView, GetCurrencies useCaseLocal, GetCurrencies useCaseRemote, SetCurrencies useCaseSetLocal) {
        mCurrencyConverterView = currencyConverterView;
        mGetLocalCurrencies = useCaseLocal;
        mGetRemoteCurrencies = useCaseRemote;
        mSetLocalCurrencies = useCaseSetLocal;
    }

    @Override
    public void create() {
        boolean updateSchedule = false;
        updateMarketRatesTime();

        if (!Config.isUpdateCurrencyFirstTime()) {
            loadLocalData();
            updateSchedule = true;
        }
        if (Config.isCurrencyUpdateRequired()) {
            fetchMarketRatesFromNetwork();
        } else if (updateSchedule) {
            scheduleMarketRateUpdate();
        }
    }

    private void fetchMarketRatesFromNetwork() {
        mGetRemoteCurrencies.execute(new Callback<String>() {
            @Override
            public void onSuccess(String jsonData) {
                Log.d(TAG,jsonData);
                mSetLocalCurrencies.updateDataLocally(jsonData);
                Config.setCurrencyRateNow();
                scheduleMarketRateUpdate();
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(TAG, "onFailure");
                error.printStackTrace();
                mCurrencyConverterView.scheduleMarketRateUpdate(System.currentTimeMillis()+Config.UPDATE_MARTE_RATE_FAILURE_INTERNAL);
            }
        });
    }

    private void scheduleMarketRateUpdate() {
        long updateTime = Config.getLastUpdatedTime() + Config.getUpdateInternal();
        long now = System.currentTimeMillis();

        if (now < updateTime) {
            mCurrencyConverterView.scheduleMarketRateUpdate(updateTime);
        } else {
            mCurrencyConverterView.scheduleMarketRateUpdate(now);
        }
    }

    private void updateMarketRatesTime() {
        Date date = new Date(Config.getLastUpdatedTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mCurrencyConverterView.setLastUpdated(sdf.format(date));
    }

    private void loadLocalData() {
        mCurrencyList = new ArrayList<>();
        mGetLocalCurrencies.execute(new Callback<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> list) {
                mCurrencyList = list;
                List<String> currencyTypes = new ArrayList<String>();
                for (Currency currency : list) {
                    currencyTypes.add(currency.getName());
                }
                mCurrencyConverterView.setCurrencyTypes(currencyTypes);
                mCurrencyConverterView.setCurrencies(mCurrencyList);
                updateMarketRatesTime();
            }

            @Override
            public void onFailure(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void pause() {
        EventBus.getDefault().unregister(this);
        mGetLocalCurrencies.stop();
        mGetRemoteCurrencies.stop();
    }

    @Override
    public void resume() {
        EventBus.getDefault().register(this);
        scheduleMarketRateUpdate();
    }

    public void onEventMainThread(EventDataUpdated event) {
        loadLocalData();
    }

    public void onEventMainThread(EventMarketRatesUpdateRequired event) {
        fetchMarketRatesFromNetwork();
    }
}
