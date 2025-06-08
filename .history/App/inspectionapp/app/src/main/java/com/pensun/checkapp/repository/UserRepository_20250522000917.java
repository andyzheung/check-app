package com.pensun.checkapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;
import com.pensun.checkapp.model.User;
import com.pensun.checkapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public LiveData<LoginResponse> login(LoginRequest request) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        RetrofitClient.getInstance().getApiService().login(request)
            .enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    data.postValue(response.body());
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    data.postValue(null);
                }
            });
        return data;
    }

    public LiveData<User> getCurrentUser(String token) {
        MutableLiveData<User> data = new MutableLiveData<>();
        RetrofitClient.getInstance().getApiService().getCurrentUser(token)
            .enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    data.postValue(null);
                }
            });
        return data;
    }
} 