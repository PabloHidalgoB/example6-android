package com.retailsbs.example6.CustomClasses;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.retailsbs.example6.Activities.RootActivity;
import com.retailsbs.example6.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by miguelangelbravo on 19/11/2016.
 */

public class WebServiceClient extends AsyncTask<Context, Void, JSONObject> {
    private SweetAlertDialog mSpinnerDialog = null;
    private static final int TIMEOUT_CONNECTION = 10000;
    private static final int TIMEOUT_SOCKET = 10000;
    private String sUrlParameters = "";
    private RootActivity activity;
    private RequestInterface activityReply;
    private String sUrlBase;
    private String sUrl;
    private String sRequestMethod;
    public String TAG;
    public boolean timeout = false;

    // reply
    public boolean success = true;
    public JSONObject data;

    public WebServiceClient(RootActivity activity, RequestInterface activityReply, SweetAlertDialog mSpinnerDialog, String tag) {
        this.mSpinnerDialog = mSpinnerDialog;
        this.activity = activity;
        this.TAG = tag;
        this.activityReply = activityReply;

        // default values
        sRequestMethod = "POST";

        sUrlBase = "http://api.fixer.io/latest";

    }

    public void getCurrency() {
        Log.d(activity.TAG, "=========> getCurrency");
        //String parameters = "{\"email\":\"" + user.trim() + "\", \"password\":\"" + password.trim() + "\"}";

        sUrl = "";
        sUrlParameters = null;
    }

    public void getCurrencyBase(String currencyType) {
        Log.d(activity.TAG, "=========> getCurrencyBase");
        //String parameters = "{\"email\":\"" + user.trim() + "\", \"password\":\"" + password.trim() + "\"}";

        sUrl = "?base="+ currencyType;
        sUrlParameters = null;
    }


    // Create an HostnameVerifier that hardwires the expected hostname.
    // Note that is different than the URL's hostname:
    // example.com versus example.org
    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
        }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(Context... params) {
        URL url;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        if(!LibraryUtilities.thereConnection(activity)){
            success = false;
            Log.d(activity.TAG, "==> No hay conexión a la red!!");
            LibraryUtilities.showDialogMessage(activity, activity.getString(R.string.message_not_net_connection));

        } else {
            try {

                // create the HttpURLConnection
                String urlBasePath = sUrlBase   ;

                url = new URL(urlBasePath + sUrl);
                trustAllHosts();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.setHostnameVerifier(DO_NOT_VERIFY);

                // seconds to respond
                connection.setConnectTimeout(TIMEOUT_CONNECTION);
                connection.setReadTimeout(TIMEOUT_SOCKET);

                // just want to do an HTTP POST here
                connection.setRequestMethod(sRequestMethod);
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setDoInput(true);
                if (sRequestMethod == "POST")
                    connection.setDoOutput(true);

                // bufer with encoding
                if (sUrlParameters != null) {
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                    writer.write(sUrlParameters);
                    writer.close();
                    wr.close();
                }

                // log the connection
                Log.d(activity.TAG, "==> url: " + url + ", params: " + sUrlParameters);

                // connect
                connection.connect();

                int responseCode = connection.getResponseCode();

                Log.d(activity.TAG, "==> RequestProperty \"Authorization\": " + connection.getRequestProperty("Authorization"));
                Log.d(activity.TAG, "==> Sending '" + connection.getRequestMethod() + "' request to URL : " + url);
                Log.d(activity.TAG, "==> Post parameters : " + sUrlParameters);
                Log.d(activity.TAG, "==> Response Code : " + responseCode);

                // read the output from the server
                if (responseCode == 200)
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                else
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // connect
                connection.disconnect();

                data = new JSONObject(stringBuilder.toString());

                return data;

            } catch (SocketTimeoutException e) {
                success = false;
                timeout = true;
                Log.d(activity.TAG, "SocketTimeoutException: " + e + " (001) " + TAG);
                LibraryUtilities.showDialogMessage(activity, activity.getString(R.string.message_error_connection_to_server) + " (001-1)");

            } catch (JSONException e) {
                success = false;
                Log.d(activity.TAG, "JSONException: " + e.getMessage() + " (002) " + TAG);
                Log.d(activity.TAG, "JSONException: " + e + " (002) " + TAG);
                LibraryUtilities.showDialogMessage(activity, activity.getString(R.string.message_error_connection_to_server) + " (002-1)");

            } catch (UnsupportedEncodingException e) {
                success = false;
                Log.d(activity.TAG, "UnsupportedEncodingException: " + e + " (004) " + TAG);
                LibraryUtilities.showDialogMessage(activity, activity.getString(R.string.message_error_connection_to_server) + " (004-1)");

            } catch (IOException e) {
                success = false;
                Log.d(activity.TAG, "IOException: " + e + " (005) " + TAG);
                LibraryUtilities.showDialogMessage(activity, activity.getString(R.string.message_error_connection_to_server) + " (005-1)");

            } finally {
                // close the reader; this can throw an exception too, so
                // wrap it in another try/catch block.
                if (reader != null){
                    try {
                        reader.close();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        try {
            if(mSpinnerDialog != null)
                mSpinnerDialog.dismiss();

            Log.d(activity.TAG, "==> onPostExecute data: " + data);
            Log.d(activity.TAG, "==> request.success: " + success);

            activityReply.resultRequest(this);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


