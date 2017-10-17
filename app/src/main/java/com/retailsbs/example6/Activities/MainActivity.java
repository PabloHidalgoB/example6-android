package com.retailsbs.example6.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.retailsbs.example6.CustomClasses.LibraryUtilities;
import com.retailsbs.example6.CustomClasses.RequestInterface;
import com.retailsbs.example6.CustomClasses.WebServiceClient;
import com.retailsbs.example6.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends RootActivity implements RequestInterface  {

    public RootActivity mRoot;

    private Button mConvert;
    private EditText mInputValue;
    private TextView mOutputValue;

    private Spinner mCurrencyInput;
    private Spinner mCurrencyOutput;

    private static final String TAG ="Gondola";
    private static final String WSC_GET_RELATED_PRODUCT ="wsc_get_related_product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConvert = (Button) findViewById(R.id.btn_convert);
        mInputValue = (EditText) findViewById(R.id.txt_input);
        mOutputValue = (TextView) findViewById(R.id.txt_value);

        mCurrencyInput = (Spinner) findViewById(R.id.spn_input);
        mCurrencyOutput = (Spinner) findViewById(R.id.spn_convert);





        mConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SweetAlertDialog mSpinnerDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSpinnerDialog.setTitleText(getString(R.string.message_connecting));
        mSpinnerDialog.setCancelable(false);
        WebServiceClient request = new WebServiceClient(MainActivity.this, MainActivity.this, mSpinnerDialog, WSC_GET_RELATED_PRODUCT);
        request.getCurrency();
        request.execute();
        mSpinnerDialog.show();

    }


    public void resultRequest(WebServiceClient request){
        if (request.TAG.contentEquals(WSC_GET_RELATED_PRODUCT)) {
            if (!request.success)
                LibraryUtilities.showDialogMessage(this, getString(R.string.message_error_connection_to_server));

            else {
                try {
                    JSONObject jsonObject = new JSONObject(request.data.toString());
                    Log.d("TAG", jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
