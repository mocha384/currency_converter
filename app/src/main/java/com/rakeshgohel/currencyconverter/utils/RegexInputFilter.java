package com.rakeshgohel.currencyconverter.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rgohel on 2017-03-26.
 */

public class RegexInputFilter implements InputFilter {
    private Pattern mPattern;

    public RegexInputFilter(String pattern) {
        this(Pattern.compile(pattern));
    }

    public RegexInputFilter(Pattern pattern) {
        mPattern = pattern;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuffer builder = new StringBuffer(dest);
        builder.replace(dstart, dend, source.subSequence(start,end).toString());
        Matcher matcher = mPattern.matcher(builder.toString());
        if (!matcher.matches()) {
            if (source.length() == 0) {
                return dest.subSequence(dstart,dend);
            }
            return "";
        }
        return null;
    }
}
