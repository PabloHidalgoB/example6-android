package com.retailsbs.example6.CustomClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.retailsbs.example6.Activities.RootActivity;
import com.retailsbs.example6.R;

/**
 * Created by miguelangelbravo on 08/08/2017.
 */

public class LibraryUtilities {

    public static boolean thereConnection(Context context) {
        NetworkInfo net_info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return net_info != null && net_info.isAvailable() && net_info.isConnected();

    }

    public static void showDialogMessage(final RootActivity activity, final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle(activity.getString(R.string.message_generic_title));
                dialog.setMessage(message);
                dialog.setPositiveButton(activity.getString(R.string.text_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        activity.hideKeyboard(activity);
                    }
                });
            }

        });
    }


}
