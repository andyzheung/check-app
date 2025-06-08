package com.pensun.checkapp.network;

import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;
import com.pensun.checkapp.model.Record;
import com.pensun.checkapp.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {
    @POST("/api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/records")
    Call<List<Record>> getRecords(@Header("Authorization") String token);

    @GET("/api/users/current")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    // 扫码上传接口（假设）
    @POST("/api/scan")
    Call<Void> uploadScanResult(@Header("Authorization") String token, @Query("code") String code);
} 