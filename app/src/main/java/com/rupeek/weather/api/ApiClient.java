package com.rupeek.weather.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rupeek.weather.model.request.WeatherRequest;
import com.rupeek.weather.model.response.WeatherResponse;
import com.rupeek.weather.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.rupeek.weather.utils.LogUtils.addErrorToLogs;
import static com.rupeek.weather.utils.LogUtils.addInfoToLogs;

public class ApiClient {
    private static final String TAG = ApiClient.class.getSimpleName();
    private ApiService apiInterface;
    //    private PrefManager prefManager;
    private Retrofit retrofit;
    private String BASE_URL = "";
    private static ApiClient mApiClient;

    public ApiClient(Context context, String base_url) {
//        prefManager = PrefManager.getInstance(context);
        this.BASE_URL = base_url;
//        addInfoToLogs(TAG, "   URL:- " + BASE_URL);
        try {
            retrofit = getRetrofit();
            apiInterface = retrofit.create(ApiService.class);
        } catch (Exception e) {
            ToastUtils.showToastShort(context, "Api Client Error");
            addErrorToLogs(TAG, "Api Client Error", e);
        }
    }

    public static ApiClient getInstance(Context context, String baseUrl) {
        mApiClient = new ApiClient(context, baseUrl);
        addInfoToLogs(TAG, "base" + baseUrl);
        return mApiClient;
    }

    public Retrofit getRetrofit() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        clientBuilder.readTimeout(300, TimeUnit.SECONDS);
        clientBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
//                        .header("Authorization", prefManager.getPreference(TOKEN.name(), ""))
                        .method(original.method(), original.body())
                        .build();
                assert original.body() != null;
                addInfoToLogs(TAG, "Request method--->>> " + request.method()
                        + "\nisHttps--->>> " + request.isHttps()
                        + "\nurl--->>> " + request.url()
                        + "\nheaders--->>> " + request.headers()
                        + "\nbody--->>> " + request.body());
                return chain.proceed(request);
            }
        });
        Gson gson = new GsonBuilder()
                .setLenient()
                .disableHtmlEscaping()
                .create();
        OkHttpClient client = clientBuilder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    public Call<WeatherResponse> callWeatherDataRequest(WeatherRequest request) {
        addInfoToLogs(TAG, "callWeatherDataRequest URL Called:-" + apiInterface.getWeatherData(request).request().url());
        addInfoToLogs(TAG, "callWeatherDataRequest -> " + request.toString());
        return apiInterface.getWeatherData(request);
    }

    public Call<WeatherResponse> callBackupWeatherDataRequest(WeatherRequest request) {
        addInfoToLogs(TAG, "callBackupWeatherDataRequest URL Called:-" + apiInterface.getWeatherDataFromSecondaryApi(request).request().url());
        addInfoToLogs(TAG, "callBackupWeatherDataRequest -> " + request.toString());
        return apiInterface.getWeatherDataFromSecondaryApi(request);
    }
}
