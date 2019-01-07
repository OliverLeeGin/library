package wenavi.jp.nal.loginlibrary.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Copyright © Nals
 * Created by TrangLT on 21/03/18.
 */
public final class ApiService {

    private static ApiService sInstance;
    private Retrofit mRetrofit;
    private Api mApi;
    private static final int CONNECT_TIMEOUT_MILLS = 1000 * 120;

    private ApiService() {
        mRetrofit = new Retrofit.Builder().baseUrl("http://5c330d61e0948000147a7722.mockapi.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClientBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApi = mRetrofit.create(Api.class);
    }

    /**
     * Get instance of ApiService object
     *
     * @return sInstance
     */
    public static ApiService getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new ApiService();
        }
        return sInstance;
    }

    private OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                .readTimeout(CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClientBuilder.addInterceptor(interceptor);
        return okHttpClientBuilder;
    }

    /**
     * Get instance of ApiService with out init.
     * Please take care when using it before init.
     *
     * @return sInstance
     */
    public static ApiService getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("Please init Api Service before using !!!!");
        }
        return sInstance;
    }

    Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Api getApi() {
        return mApi;
    }
}
