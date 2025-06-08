package com.inspection.app.network;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // 用户认证相关
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/logout")
    Call<LogoutResponse> logout();

    // 用户信息相关
    @GET("users/profile")
    Call<UserProfileResponse> getUserProfile();

    @PUT("users/profile")
    Call<UserProfileResponse> updateUserProfile(@Body UserProfileRequest request);

    // 巡检记录相关
    @POST("inspections")
    Call<InspectionResponse> createInspection(@Body InspectionRequest request);

    @GET("inspections")
    Call<InspectionListResponse> getInspections(
        @Query("page") int page,
        @Query("pageSize") int pageSize
    );

    @GET("inspections/area/{areaId}")
    Call<InspectionListResponse> getInspectionsByArea(
        @Path("areaId") long areaId,
        @Query("page") int page,
        @Query("pageSize") int pageSize
    );

    @GET("inspections/user/{userId}")
    Call<InspectionListResponse> getInspectionsByUser(
        @Path("userId") long userId,
        @Query("page") int page,
        @Query("pageSize") int pageSize
    );

    // 区域相关
    @GET("areas")
    Call<AreaListResponse> getAreas();

    @GET("areas/{areaId}")
    Call<AreaResponse> getAreaDetail(@Path("areaId") long areaId);

    @GET("areas/qrcode/{qrCode}")
    Call<AreaResponse> getAreaByQRCode(@Path("qrCode") String qrCode);
} 