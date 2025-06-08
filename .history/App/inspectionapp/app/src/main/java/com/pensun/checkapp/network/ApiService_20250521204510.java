package com.pensun.checkapp.network;

import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);
} 