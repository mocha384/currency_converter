package com.rakeshgohel.currencyconverter.repository;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rakeshgohel.currencyconverter.data.Config;
import com.rakeshgohel.currencyconverter.data.DaoSessionManager;
import com.rakeshgohel.currencyconverter.events.EventDataUpdated;
import com.rakeshgohel.currencyconverter.models.Currency;
import com.rakeshgohel.currencyconverter.models.CurrencyDao;
import com.rakeshgohel.currencyconverter.models.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import de.greenrobot.event.EventBus;
import io.reactivex.Single;

/**
 * Created by rgohel on 2017-03-25.
 */

public class LocalCurrencyRepositoryImpl implements LocalCurrencyRepository {
    private static final String TAG = LocalCurrencyRepositoryImpl.class.getSimpleName();

    private final DaoSession mDaoSession;

    public LocalCurrencyRepositoryImpl() {
        mDaoSession = DaoSessionManager.getSession();
    }
    @Override
    public Single<List<Currency>> getCurrencies() {
        return Single.fromCallable(new Callable<List<Currency>>() {
            @Override
            public List<Currency> call() throws Exception {
                return getCurrenciesFromDatabase();
            }
        });
    }

    private List<Currency> getCurrenciesFromDatabase() {
        CurrencyDao currencyDao = mDaoSession.getCurrencyDao();
        Query<Currency> currencyQuery = currencyDao.queryBuilder().build();
        return currencyQuery.list();
    }

    @Override
    public void setCurrencies(String jsonData) {
        parseAndUpdateDatabase(jsonData);
    }

    private void parseAndUpdateDatabase(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(json);

        boolean insert = mDaoSession.getCurrencyDao().queryBuilder().list().size() == 0;

        if (!jsonTree.isJsonNull()) {
            if (jsonTree.isJsonObject()) {
                JsonObject jsonObject = jsonTree.getAsJsonObject();
                JsonElement base = jsonObject.get("base");
                String baseValue = base.getAsString();
                insertOrUpdateDatabase(baseValue, Double.parseDouble(Config.BASE_CURRENCY_EUR),getDecimal(Config.BASE_CURRENCY_EUR),insert);
                JsonElement date = jsonObject.get("date");
                JsonElement rates = jsonObject.get("rates");
                if (rates.isJsonObject()) {
                    JsonObject ratesObj = rates.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> entries = ratesObj.entrySet();
                    for (Map.Entry<String, JsonElement> entry : entries) {
                        String value = entry.getValue().getAsString();
                        insertOrUpdateDatabase(entry.getKey(), Double.parseDouble(value),getDecimal(value),insert);
                    }
                }
                EventBus.getDefault().post(new EventDataUpdated());
            }
        }
    }

    private void insertOrUpdateDatabase(String name, Double value, int decimal, boolean insert) {
        Currency currency = new Currency(null, name, value, decimal);
        if (insert) {
            mDaoSession.getCurrencyDao().insert(currency);
        } else {
            List<Currency> currencies = mDaoSession.getCurrencyDao().queryBuilder().where(CurrencyDao.Properties.Name.eq(name)).list();
            if (currencies.size() == 1) {
                currencies.get(0).setValue(value);
                mDaoSession.getCurrencyDao().update(currencies.get(0));
            } else {
                Log.d(TAG, "Duplicate currencies " + name);
            }
        }
    }

    private int getDecimal(String num) {
        int index = num.lastIndexOf('.');
        if (index >= 0) {
            index++;
        }
        return num.length() - index;
    }
}
