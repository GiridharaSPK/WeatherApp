package com.rupeek.weather.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.rupeek.weather.utils.LogUtils.addErrorToLogs;

public class FragmentUtils {
    private static final String TAG = FragmentUtils.class.getSimpleName();

    public static void setFragment(Context context, int replaceViewId, Bundle bundle, boolean backPressed, Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            if (bundle != null)
                fragment.setArguments(bundle);
            FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(replaceViewId, fragment);
            if (backPressed)
                transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception ex) {
            addErrorToLogs(TAG, "setFragment", ex);
        }
    }
}
