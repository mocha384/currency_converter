package com.rakeshgohel.currencyconverter.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rakeshgohel.currencyconverter.R;
import com.rakeshgohel.currencyconverter.models.Currency;
import com.rakeshgohel.currencyconverter.presenters.PresenterCurrencyActivity;
import com.rakeshgohel.currencyconverter.presenters.PresenterCurrencyActivityImpl;
import com.rakeshgohel.currencyconverter.ui.recycler.AdapterCurrencyRecyclerView;
import com.rakeshgohel.currencyconverter.ui.views.ViewCurrencyConverter;
import com.rakeshgohel.currencyconverter.utils.AlarmReceiver;
import com.rakeshgohel.currencyconverter.utils.RegexInputFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgohel on 2017-03-25.
 */
public class ActivityCurrency extends AppCompatActivity implements ViewCurrencyConverter {

    private static final String TAG = ActivityCurrency.class.getSimpleName();

    private EditText                    mEditTextNumber;
    private Spinner                     mSpinnerCurrencyType;
    private RecyclerView                mRecyclerViewCurrencies;
    private AdapterCurrencyRecyclerView mAdapterCurrency;
    private ArrayAdapter<String>        mAdapterCurrencyType;
    private PresenterCurrencyActivity   mPresenterCurrencyActivity;
    private List<String>                mListCurrencyType;
    private List<Currency>              mListCurrency;
    private TextView                    mTextViewLastUpdated;
    private PendingIntent               mPendingIntent;
    private AlarmManager                mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        mEditTextNumber         = (EditText)     findViewById(R.id.activity_currency_edittext_number);
        mSpinnerCurrencyType    = (Spinner)      findViewById(R.id.activity_currency_spinner_currency_type);
        mRecyclerViewCurrencies = (RecyclerView) findViewById(R.id.activity_currency_recyclerview_currency_list);


        mEditTextNumber.setFilters(new InputFilter[] {new RegexInputFilter("[0-9]{0,8}+((\\.[0-9]{0,2})?)||(\\.)?")});
        mEditTextNumber.setText("1.00");
        mEditTextNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setCurrenciesInAdapter();
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerViewCurrencies.setLayoutManager(layoutManager);
        mRecyclerViewCurrencies.setItemAnimator(new DefaultItemAnimator());

        mAdapterCurrency = new AdapterCurrencyRecyclerView();
        mRecyclerViewCurrencies.setAdapter(mAdapterCurrency);

        mListCurrencyType = new ArrayList<>();
        mAdapterCurrencyType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mListCurrencyType);
        mAdapterCurrencyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCurrencyType.setAdapter(mAdapterCurrencyType);

        mSpinnerCurrencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                setCurrenciesInAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "onNothingSelected");
            }
        });

        mTextViewLastUpdated = (TextView) findViewById(R.id.activity_currency_textview_last_updated);

        mAlarmManager = (AlarmManager) ActivityCurrency.this.getSystemService(Context.ALARM_SERVICE);

        mPresenterCurrencyActivity = new PresenterCurrencyActivityImpl(this);
        mPresenterCurrencyActivity.create();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPendingIntent != null) {
            mAlarmManager.cancel(mPendingIntent);
        }
        mPresenterCurrencyActivity.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenterCurrencyActivity.resume();
    }

    @Override
    public void setCurrencyTypes(List<String> currencyTypeList) {
        if (mAdapterCurrencyType != null) {
            mAdapterCurrencyType.clear();
            mAdapterCurrencyType.addAll(currencyTypeList);
            mListCurrencyType = currencyTypeList;
        }
    }

    @Override
    public void setCurrencies(List<Currency> currencies) {
        if (mAdapterCurrency != null) {
            mListCurrency = currencies;
            setCurrenciesInAdapter();
        }
    }

    @Override
    public void setLastUpdated(String timeFormatted) {
        mTextViewLastUpdated.setText(getString(R.string.market_update) + timeFormatted);
    }

    @Override
    public void scheduleMarketRateUpdate(long alarmTime) {
        Intent alarmIntent = new Intent(ActivityCurrency.this, AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(ActivityCurrency.this, 0, alarmIntent, 0);

        mAlarmManager.setExact(AlarmManager.RTC, alarmTime, mPendingIntent);
    }

    private void setCurrenciesInAdapter() {
        String userInput = mEditTextNumber.getText().toString().trim();
        Double userInputValue = userInput.isEmpty()? 1.0 : Double.parseDouble(userInput);
        String selectedSpinnerItem = String.valueOf(mSpinnerCurrencyType.getSelectedItem());
        Double baseRate = 1.0;
        for (Currency currency : mListCurrency) {
            if (currency.getName().compareTo(selectedSpinnerItem) == 0) {
                baseRate = currency.getValue();
                break;
            }
        }
        mAdapterCurrency.setItems(userInputValue, baseRate, mListCurrency);
    }
}
