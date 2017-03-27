package com.rakeshgohel.currencyconverter.ui.views;

import com.rakeshgohel.currencyconverter.models.Currency;

import java.util.List;

/**
 * Created by rgohel on 2017-03-25.
 */

public interface CurrencyConverterView {
    void setCurrencyTypes(List<String> currencyTypes);
    void setCurrencies(List<Currency> currencies);
    void setLastUpdated(String time);
    void scheduleMarketRateUpdate(long alarmTime);
}
