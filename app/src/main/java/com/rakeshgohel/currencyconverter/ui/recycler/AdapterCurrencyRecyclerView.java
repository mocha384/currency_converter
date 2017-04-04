package com.rakeshgohel.currencyconverter.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rakeshgohel.currencyconverter.R;
import com.rakeshgohel.currencyconverter.models.Currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgohel on 2017-03-25.
 */

public class AdapterCurrencyRecyclerView extends RecyclerView.Adapter<AdapterCurrencyRecyclerView.CurrencyViewHolder> {
    private List<CurrencyItem> mCurrencyItemList;
    private double             mBaseRate;
    private double             mUserInputValue;
    private DecimalFormat      mDecimalFormat;

    public AdapterCurrencyRecyclerView() {
        mCurrencyItemList = new ArrayList<>();
        mDecimalFormat = new DecimalFormat("#.####");
        mDecimalFormat.setRoundingMode(RoundingMode.CEILING);
    }
    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.activity_currency_recyclerview_item, parent, false);
        return new CurrencyViewHolder(itemView);
    }

    public void setItems(double userInputValue, double baseRate, List<Currency> itemList) {
        mUserInputValue = userInputValue;
        mBaseRate = baseRate;
        mCurrencyItemList.clear();
        for (Currency currency : itemList) {
            mCurrencyItemList.add(new CurrencyItem(currency.name,
                                                   currency.value/mBaseRate,
                                                   mUserInputValue, mBaseRate, currency.getDecimal()));
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        final CurrencyItem currencyItem = mCurrencyItemList.get(position);
        if (currencyItem != null) {
            holder.textViewName.setText(currencyItem.name);
            holder.textViewValue.setText(mDecimalFormat.format(currencyItem.value));
            holder.textViewCalcValue.setText(mDecimalFormat.format(currencyItem.calculatedValue));
        }
    }

    @Override
    public int getItemCount() {
        return mCurrencyItemList.size();
    }

    final static class CurrencyItem {
        String name;
        double value;
        double calculatedValue;

        public CurrencyItem(String name, double value, double input, double baseRate, int decimal) {
            this.name = name;
            this.value = value;
            this.calculatedValue = value*input;
        }
    }

    final static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewValue;
        TextView textViewCalcValue;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.activity_currency_recyclerview_item_name);
            textViewValue = (TextView) itemView.findViewById(R.id.activity_currency_recyclerview_item_value);
            textViewCalcValue = (TextView) itemView.findViewById(R.id.activity_currency_recyclerview_item_calculated_value);
        }
    }
}
