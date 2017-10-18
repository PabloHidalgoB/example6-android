package com.retailsbs.example6.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.retailsbs.example6.CustomClasses.CurrencyPDB;

import java.util.ArrayList;

/**
 * Created by Trabajo on 18-10-2017.
 */

public class TodayCurrencyAdapter extends ArrayAdapter<CurrencyPDB> {

    public TodayCurrencyAdapter(Context mContext, int resource, ArrayList<CurrencyPDB> mArrayList) {
        super(mContext, resource, mArrayList);


    }
}
