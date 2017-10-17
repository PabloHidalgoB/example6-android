package com.retailsbs.example6.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.retailsbs.example6.CustomClasses.CurrencyPDB;
import com.retailsbs.example6.CustomClasses.LibraryUtilities;
import com.retailsbs.example6.CustomClasses.RequestInterface;
import com.retailsbs.example6.CustomClasses.WebServiceClient;
import com.retailsbs.example6.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends RootActivity implements RequestInterface {

    public RootActivity mRoot;

    private Button mConvert;
    private EditText mInputValue;
    private TextView mOutputValue;

    private Spinner mCurrencyInput;
    private Spinner mCurrencyOutput;

    private ArrayList<CurrencyPDB> mList = new ArrayList<>();
    private ArrayList<String> mAdapter = new ArrayList<String>();

    private static final String TAG = "Example6";
    private static final String WSC_GET_RELATED_PRODUCT = "wsc_get_related_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConvert = (Button) findViewById(R.id.btn_convert);
        mInputValue = (EditText) findViewById(R.id.txt_input);
        mOutputValue = (TextView) findViewById(R.id.txt_value);

        mCurrencyInput = (Spinner) findViewById(R.id.spn_input);
        mCurrencyOutput = (Spinner) findViewById(R.id.spn_convert);


        //SweetAlertDialog mSpinnerDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        //mSpinnerDialog.setTitleText(getString(R.string.message_connecting));
        //mSpinnerDialog.setCancelable(false);
        WebServiceClient request = new WebServiceClient(MainActivity.this, MainActivity.this, null, WSC_GET_RELATED_PRODUCT);
        request.getCurrency();
        request.execute();
        //mSpinnerDialog.show();




        mConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInputValue.getText();
                mList.get(mCurrencyInput.getSelectedItemPosition()).getmRate();
                mList.get(mCurrencyOutput.getSelectedItemPosition()).getmRate();


                //mOutputValue.setText(); TODO setear valor en base al valor ingresado multiplicado por el rate de la moneda pedida

            }
        });

    }


    public void resultRequest(WebServiceClient request) {
        if (request.TAG.contentEquals(WSC_GET_RELATED_PRODUCT)) {
            if (!request.success)
                LibraryUtilities.showDialogMessage(this, getString(R.string.message_error_connection_to_server));

            else {
                try {
                    JSONObject jsonObject = new JSONObject(request.data.toString());
                    Log.d("TAG", jsonObject.toString());

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

                        Log.d("CODE", jsonObjectY.getString("code"));
                        Log.d("CODE", jsonObjectY.getString("alphaCode"));
                        Log.d("CODE", jsonObjectY.getString("numericCode"));
                        Log.d("CODE", jsonObjectY.getString("name"));
                        Log.d("CODE", jsonObjectY.getString("rate"));
                        Log.d("CODE", jsonObjectY.getString("date"));

                        mList.add(mCurrencyPDB);
                    }

                    loadData();

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
}
