package com.rakeshgohel.currencyconverter.data;

import android.content.Context;

import com.rakeshgohel.currencyconverter.models.DaoMaster;
import com.rakeshgohel.currencyconverter.models.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by rgohel on 2017-03-25.
 */

public class DaoSessionManager {
    private static DaoSession mDaoSession;

    public static DaoSession getSession() {
        return mDaoSession;
    }

    public static void init(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "currency-db");
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
    }
}
