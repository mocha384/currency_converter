package com.rakeshgohel.currencyconverter.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rgohel on 2017-03-26.
 */

public class Config {
    private static final String SHARED_PREFERENCE_NAME = "currency_prefs";
    private static final int UPDATE_MARKET_RATE_INTERNAL = 1*60*1000;

    public static final int UPDATE_MARTE_RATE_FAILURE_INTERNAL = 1*60*1000;

    public static final String PREFERENCE_LAST_CURRENCY_RATE_UPDATE = "PREFERENCE_LAST_CURRENCY_RATE_UPDATE";
    public static final String BASE_CURRENCY_EUR = "1.0000";

    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void setPreference(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getPreference(String key) {
        synchronized (mContext) {
            return mSharedPreferences.getString(key, "");
        }
    }

    public static boolean hasPreference(String key) {
        synchronized (mContext) {
            return mSharedPreferences.contains(key);
        }
    }

    public static void setCurrencyRateNow()  {
        setPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE, Long.toString(System.currentTimeMillis()));
    }

    public static boolean isCurrencyUpdateRequired() {
        synchronized (mContext) {
            boolean update = false;
            if (hasPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE)) {
                long now = System.currentTimeMillis();
                long lastUpdateTime = Long.parseLong(getPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE));
                if (now > lastUpdateTime + UPDATE_MARKET_RATE_INTERNAL) {
                    update = true;
                }
            } else {
                update = true;
            }
            return update;
        }
    }

    public static long getLastUpdatedTime() {
        if (hasPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE)) {
            return Long.parseLong(getPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE));
        }
        return 0;
    }

    public static long getUpdateInternal() {
        return UPDATE_MARKET_RATE_INTERNAL;
    }

    public static boolean isUpdateCurrencyFirstTime() {
        synchronized (mContext) {
            return !hasPreference(PREFERENCE_LAST_CURRENCY_RATE_UPDATE);
        }
    }
}
