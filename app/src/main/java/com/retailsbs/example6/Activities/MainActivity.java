package com.retailsbs.example6.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.retailsbs.example6.Adapters.TodayCurrencyAdapter;
import com.retailsbs.example6.CustomClasses.CurrencyPDB;
import com.retailsbs.example6.CustomClasses.LibraryUtilities;
import com.retailsbs.example6.CustomClasses.RequestInterface;
import com.retailsbs.example6.CustomClasses.WebServiceClient;
import com.retailsbs.example6.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends RootActivity implements RequestInterface {

    private
    Double clpRate = 0.0;


    private Button mConvert;
    private Button mSwitch;

    private EditText mInputValue;
    private TextView mOutputValue;

    private Spinner mCurrencyInput;
    private Spinner mCurrencyOutput;

    private ArrayList<CurrencyPDB> mList = new ArrayList<>();
    private ArrayList<CurrencyPDB> mListAux = new ArrayList<>();


    private ArrayList<String> mAdapter = new ArrayList<String>();

    private TodayCurrencyAdapter mTodayCurrencyAdapter;
    private ListView mListView;

    private ArrayList<Integer> mListBills;
    private ArrayList<String> mCurrencyToday;


    private static final String WSC_GET_RELATED_PRODUCT = "wsc_get_related_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Convertidor de Monedas via JSON");

        mConvert = (Button) findViewById(R.id.btn_convert);
        mSwitch = (Button) findViewById(R.id.btn_switch);

        mInputValue = (EditText) findViewById(R.id.txt_input);
        mOutputValue = (TextView) findViewById(R.id.txt_value);

        mCurrencyInput = (Spinner) findViewById(R.id.spn_input);
        mCurrencyOutput = (Spinner) findViewById(R.id.spn_convert);


        mListView = (ListView) findViewById(R.id.lstv_todayCurrency);


        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#448AFF"));
        pDialog.setTitleText("Cargando datos");
        pDialog.setCancelable(false);
        WebServiceClient request = new WebServiceClient(MainActivity.this, MainActivity.this, pDialog, WSC_GET_RELATED_PRODUCT);
        request.getCurrency();
        request.execute();
        pDialog.show();


        mListBills = new ArrayList<>();
        mListBills.clear();

        mCurrencyToday = new ArrayList<>();
        mCurrencyToday.clear();


        mConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConvert();
            }
        });

        mInputValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    doConvert();
                }
                return false;
            }
        });

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int spinner1Index = mCurrencyInput.getSelectedItemPosition();

                mCurrencyInput.setSelection(mCurrencyOutput.getSelectedItemPosition());
                mCurrencyOutput.setSelection(spinner1Index);
            }
        });

        hideKeyboard();
    }

    private void load() {

        Parcelable mParcelable = mListView.onSaveInstanceState();
        mTodayCurrencyAdapter = new TodayCurrencyAdapter(MainActivity.this, R.layout.row_todaycurrency, mListBills, mCurrencyToday, mListAux, clpRate); //TODO setear parametros correctos
        mListView.setAdapter(mTodayCurrencyAdapter);
        mListView.onRestoreInstanceState(mParcelable);
    }


    public void resultRequest(WebServiceClient request) {
        if (request.TAG.contentEquals(WSC_GET_RELATED_PRODUCT)) {
            if (!request.success)
                LibraryUtilities.showDialogMessage(this, getString(R.string.message_error_connection_to_server));

            else {
                try {
                    JSONObject jsonObject = new JSONObject(request.data.toString());

                    mList.clear();

                    Iterator x = jsonObject.keys();
                    while (x.hasNext()) {
                        String xKey = (String) x.next();

                        CurrencyPDB mCurrencyPDB = new CurrencyPDB();
                        JSONObject jsonObjectY = new JSONObject(jsonObject.getString(xKey));
                        mCurrencyPDB.setmCode(jsonObjectY.getString("code"));
                        mCurrencyPDB.setmAlphaCode(jsonObjectY.getString("alphaCode"));
                        mCurrencyPDB.setmNumericCode(jsonObjectY.getString("numericCode"));
                        mCurrencyPDB.setmName(jsonObjectY.getString("name"));
                        mCurrencyPDB.setmRate(jsonObjectY.getDouble("rate"));
                        mCurrencyPDB.setmDate(jsonObjectY.getString("date"));

                        mList.add(mCurrencyPDB);
                    }

                    loadData();
                    getCurrencies();
                    load();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadData(){
        mAdapter.clear();
        for (int x = 0; x < mList.size(); x++)
        {
            mAdapter.add(mList.get(x).getmName());
        }
        mCurrencyInput.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, mAdapter));
        mCurrencyOutput.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, mAdapter));
    }

    private void getCurrencies(){

        mListAux.clear();

        for ( int x = 0; x < mList.size(); x++){

            if ((mList.get(x).getmCode().contentEquals("USD")))
            {
                mListAux.add(mList.get(x));
                mCurrencyToday.add("El Dólar hoy esta a:");
                mListBills.add(R.mipmap.usdollar);

            } else if ((mList.get(x).getmCode().contentEquals("EUR"))){

                mListAux.add(mList.get(x));
                mCurrencyToday.add("El Euro hoy esta a:");
                mListBills.add(R.mipmap.europeaneuro);

            } else if ((mList.get(x).getmCode().contentEquals("GBP"))){

                mListAux.add(mList.get(x));
                mCurrencyToday.add("La Libra Esterlina hoy esta a:");
                mListBills.add(R.mipmap.ukpound);

            } else if ((mList.get(x).getmCode().contentEquals("JPY"))){

                mListAux.add(mList.get(x));
                mCurrencyToday.add("El Yen hoy esta a:");
                mListBills.add(R.mipmap.japaneseyen);

            } else if ((mList.get(x).getmCode().contentEquals("CAD"))){

                mListAux.add(mList.get(x));
                mCurrencyToday.add("El Dólar Canadiense hoy esta a:");
                mListBills.add(R.mipmap.canadadollar);

            } else if ((mList.get(x).getmCode().contentEquals("PLN"))){

                mListAux.add(mList.get(x));
                mCurrencyToday.add("El Zloty Polaco hoy esta a:");
                mListBills.add(R.mipmap.polishzloty);

            } else if ((mList.get(x).getmCode().contentEquals("CLP"))){

                clpRate = mList.get(x).getmRate();
                mCurrencyInput.setSelection(x);
            }
        }
    }

    private void doConvert(){
        Double a = 0.0;

        Double semiFinal;
        Double finalValue;

        String text = mInputValue.getText().toString();
        Double b = mList.get(mCurrencyInput.getSelectedItemPosition()).getmRate();
        Double c = mList.get(mCurrencyOutput.getSelectedItemPosition()).getmRate();

        if(!text.isEmpty())
            try
            {
                a = Double.parseDouble(text);
                // it means it is double
            } catch (Exception e1) {
                // this means it is not double
                e1.printStackTrace();
            }

        semiFinal = ((a*c)/b);
        finalValue = Math.round( semiFinal * 100.0 ) / 100.0;

        StringBuilder sb = new StringBuilder();
        Locale spanish = new Locale("es", "ES");
        Formatter formatter = new Formatter(sb, spanish);
        formatter.format("$ %(,.2f", finalValue);

        mOutputValue.setText(String.valueOf(formatter));
    }


}
