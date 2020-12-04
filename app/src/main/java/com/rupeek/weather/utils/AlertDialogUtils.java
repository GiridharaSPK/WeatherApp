package com.rupeek.weather.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.rupeek.weather.R;


public class AlertDialogUtils {
    private static AlertDialog alertDialog;

   /* public static void showProgressDialog(Context context, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setCancelable(false);
            alertDialog = builder.create();
            if (!alertDialog.isShowing())
                alertDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
            alertDialog = null;
        }
    }

    public static void hideProgressDialog() {
        try {
            if (alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
            alertDialog = null;
        }
    }*/

    /**
     * Common AppCompat Alert Dialog to be used in the Application everywhere
     *
     * @param mContext, Context of where to display
     */
    public static void displayCommonAlertDialog(Context mContext, String alertMessage) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getResources().getString(R.string.app_name));
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Common AppCompat Alert Dialog to be used in the Application everywhere
     *
     * @param mContext, Context of where to display
     */
    public static void displayCommonAlertDialog(Context mContext, boolean isTitle, String title, boolean isMessage, String alertMessage, boolean isCancelable) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            if (isTitle)
                builder.setTitle(title);
            if (isMessage)
                builder.setMessage(alertMessage);
            if (isCancelable)
                builder.setCancelable(isCancelable);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
