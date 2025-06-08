package com.inspection.app.network;

import com.inspection.app.config.AppConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(AppConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(AppConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(AppConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            }

            retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getApiBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static void resetClient() {
        retrofit = null;
        okHttpClient = null;
    }
} 