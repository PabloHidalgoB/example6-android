package com.retailsbs.example6.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.retailsbs.example6.CustomClasses.CurrencyPDB;
import com.retailsbs.example6.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Trabajo on 18-10-2017.
 */

public class TodayCurrencyAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private ArrayList<Integer> mArrayBills;
    private ImageView mRowImage;

    private TextView mCurrencyT;
    private ArrayList<String> mCurrencyToday;

    private TextView mCurrencyValue;
    private ArrayList<CurrencyPDB> mListAux;

    private Double clpRate;


    public TodayCurrencyAdapter(Context mContext, int resource, ArrayList<Integer> mArrayList, ArrayList<String> mCurrencyToday, ArrayList<CurrencyPDB> mListAux, Double clpRate) {
        super(mContext, resource, mArrayList);

        this.mContext = mContext;
        this.mArrayBills = mArrayList;
        this.mCurrencyToday = mCurrencyToday;
        this.mListAux = mListAux;
        this.clpRate = clpRate;

        Log.d("tag", clpRate.toString());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mView = convertView;


        if (mView == null){
            LayoutInflater vI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vI.inflate(R.layout.row_todaycurrency, parent, false);
        }

        mRowImage = (ImageView) mView.findViewById(R.id.img_currencyBill);
        mRowImage.setImageResource(mArrayBills.get(position));

        mCurrencyT = (TextView) mView.findViewById(R.id.txt_currencyToday);
        mCurrencyT.setText(mCurrencyToday.get(position));

        Double semiFinal = ((1*clpRate)/mListAux.get(position).getmRate());

        Double finalValue = Math.round( semiFinal * 100.0 ) / 100.0;


        mCurrencyValue = (TextView) mView.findViewById(R.id.txt_currencyValue);
        mCurrencyValue.setText("$"+ finalValue.toString());

        return mView;

    }
}
