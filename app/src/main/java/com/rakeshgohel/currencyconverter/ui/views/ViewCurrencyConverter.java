package com.rakeshgohel.currencyconverter.ui.views;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface ViewCurrencyConverter {
    void setCurrencyTypes(List<String> currencyTypes);
    void setCurrencies(List<Currency> currencies);
    void setLastUpdated(String timeFormatted);
    void scheduleMarketRateUpdate(long alarmTime);
}
