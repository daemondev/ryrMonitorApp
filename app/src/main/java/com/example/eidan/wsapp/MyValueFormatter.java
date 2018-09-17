package com.example.eidan.wsapp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MyValueFormatter implements IValueFormatter {

//    private DecimalFormat mFormat;
    private String mFormat;

//    public MyValueFormatter() {
//        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
//    }

//    @Override
//    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//        // write your logic here
//        return mFormat.format(value) + " $"; // e.g. append a dollar-sign
//    }
//    @Override
//    public String getFormattedValue(float value) {
//        return "" + ((int) value);
//    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//        return null;
        return "" + ((int) value);
    }
}
