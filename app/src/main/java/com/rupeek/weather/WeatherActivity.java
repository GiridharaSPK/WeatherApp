package com.rupeek.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rupeek.weather.adapter.WeatherAdapter;
import com.rupeek.weather.api.ApiClient;
import com.rupeek.weather.databinding.ActivityWeatherBinding;
import com.rupeek.weather.model.WeatherModel;
import com.rupeek.weather.model.request.WeatherRequest;
import com.rupeek.weather.model.response.WeatherResponse;
import com.rupeek.weather.utils.WifiUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rupeek.weather.utils.AlertDialogUtils.displayCommonAlertDialog;
import static com.rupeek.weather.utils.LogUtils.addErrorToLogs;
import static com.rupeek.weather.utils.LogUtils.addInfoToLogs;
import static com.rupeek.weather.utils.ToastUtils.showToastShort;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    private ActivityWeatherBinding activityWeatherBinding;
    private WeatherRequest request;
    private ApiClient apiClient;
    private Dialog pDialog;
    private ArrayList<WeatherModel> list = new ArrayList<>();
    private Context context;
    private WeatherAdapter adapter;
    private String latitude;
    private String longitude;
    private LocationManager locationManager;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWeatherBinding = DataBindingUtil.setContentView(WeatherActivity.this, R.layout.activity_weather);
        initView();
    }

    private void initView() {
        context = WeatherActivity.this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        request = new WeatherRequest();

        checkAndGetLocation();
        setAdapter();
        callWeatherDataApi(request);
        setupSwipeToRefresh();
    }

    private void checkAndGetLocation() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {

            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);
            /*for (Number n : Arrays.asList(12, 123.12345, 0.23, 0.1, 2341234.212431324)) {
                Double d = n.doubleValue();
                System.out.println(df.format(d));
            }*/
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            latitude = String.valueOf(df.format(lat));
            longitude = String.valueOf(df.format(longi));

            activityWeatherBinding.tvLat.setText("Lat : " + latitude);
            activityWeatherBinding.tvLong.setText("Long : " + longitude);
        } else {
            activityWeatherBinding.tvLong.setVisibility(View.GONE);
            activityWeatherBinding.tvLat.setVisibility(View.GONE);
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setAdapter() {
/*        adapter = new WeatherAdapter(context, list);
//        activityWeatherBinding.recyclerView.setHasFixedSize(true);
        activityWeatherBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        activityWeatherBinding.recyclerView.setAdapter(adapter);*/

        linearLayoutManager = new LinearLayoutManager(context);
        activityWeatherBinding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WeatherAdapter(context, list);
        activityWeatherBinding.recyclerView.setAdapter(adapter);

    }

    private void setupSwipeToRefresh() {
        activityWeatherBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addInfoToLogs(TAG, "onRefresh()");
                activityWeatherBinding.swipeToRefresh.setRefreshing(true);
                if (request != null) {
                    callWeatherDataApi(request);
                }
            }
        });
    }

    private void callWeatherDataApi(WeatherRequest request) {
        addInfoToLogs(TAG, "callWeatherDataApi");
        activityWeatherBinding.swipeToRefresh.setRefreshing(false);
        if (WifiUtils.isAvailable(context) || WifiUtils.isAvailable(context)) {

            apiClient = new ApiClient(context, "https://www.mocky.io/");
            Call<WeatherResponse> responseCallback = apiClient.callWeatherDataRequest(new WeatherRequest());
            final WeatherRequest finalRequest = request;
            responseCallback.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    hideLoading();
                    addInfoToLogs(TAG, "onResponse");
                    if (response.body() != null) {
                        addInfoToLogs(TAG, "onResponse = " + response.body());
                        updateWeatherList(response.body());
                        activityWeatherBinding.llViewData.setVisibility(View.VISIBLE);
                        activityWeatherBinding.llNoData.setVisibility(View.GONE);
                    } else {
                        addInfoToLogs(TAG, "response null");
                        activityWeatherBinding.llNoData.setVisibility(View.VISIBLE);
                        activityWeatherBinding.llViewData.setVisibility(View.GONE);
                        activityWeatherBinding.nodata.tvNoData.setText("Something went wrong");
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    addErrorToLogs(TAG, "onFailure", t);
                    hideLoading();
                    callBackupWeatherApi(finalRequest);
                    showToastShort(context, t.toString());
                    activityWeatherBinding.llViewData.setVisibility(View.GONE);
                    activityWeatherBinding.llNoData.setVisibility(View.VISIBLE);
                    activityWeatherBinding.nodata.tvNoData.setText("Something went wrong");
                }
            });
        } else {
            displayCommonAlertDialog(context, context.getString(R.string.noInternetConnection));
        }
    }

    private void callBackupWeatherApi(WeatherRequest request) {
        showLoading(getApplicationContext());
        apiClient = new ApiClient(context, "https://cee0d1b3-fa54-4bdf-b0a0-2d4cd129a521.mock.pstmn.io/");
        Call<WeatherResponse> responseCallback = apiClient.callBackupWeatherDataRequest(new WeatherRequest());
        responseCallback.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                hideLoading();
                addInfoToLogs(TAG, "onResponse");
                if (response.body() != null) {
                    addInfoToLogs(TAG, "onResponse = " + response.body());
                    updateWeatherList(response.body());
                    activityWeatherBinding.llViewData.setVisibility(View.VISIBLE);
                    activityWeatherBinding.llNoData.setVisibility(View.GONE);
                } else {
                    addInfoToLogs(TAG, "response null");
                    activityWeatherBinding.llNoData.setVisibility(View.VISIBLE);
                    activityWeatherBinding.llViewData.setVisibility(View.GONE);
                    activityWeatherBinding.nodata.tvNoData.setText("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                hideLoading();
                addErrorToLogs(TAG, "onFailure", t);
                activityWeatherBinding.llNoData.setVisibility(View.VISIBLE);
                activityWeatherBinding.llViewData.setVisibility(View.GONE);
                activityWeatherBinding.nodata.tvNoData.setText("Something went wrong");
            }
        });
    }

    private void updateWeatherList(WeatherResponse body) {
        activityWeatherBinding.recyclerView.setVisibility(View.VISIBLE);
        list.clear();
        list.addAll(body.getData());
        adapter.notifyDataSetChanged();
    }

    private void hideLoading() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private void showLoading(Context context) {
        pDialog = rotatingProgressDialog(context);
        pDialog.show();
    }

    public Dialog rotatingProgressDialog(final Context mcontext) {
        // LayoutInflater inflater = LayoutInflater.from(mcontext);
//        final Dialog mDialog = new Dialog(mcontext, android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog mDialog = new Dialog(mcontext, R.style.RotatingProgressDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        Objects.requireNonNull(mDialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        mDialog.getWindow().setAttributes(lp);
        View dialoglayout = View.inflate(mcontext,
                R.layout.progress_dialog, null);
        mDialog.setContentView(dialoglayout);
        return mDialog;
    }


}
