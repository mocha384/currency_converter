package com.rakeshgohel.currencyconverter.utils;

/**
 * Created by rgohel on 2016-03-24.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rakeshgohel.currencyconverter.events.EventMarketRatesUpdateRequired;

import de.greenrobot.event.EventBus;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new EventMarketRatesUpdateRequired());
    }
}