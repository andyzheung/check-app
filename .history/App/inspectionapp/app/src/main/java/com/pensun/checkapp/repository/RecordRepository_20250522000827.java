package com.pensun.checkapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import com.pensun.checkapp.model.Record;
import com.pensun.checkapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordRepository {
    public LiveData<List<Record>> getRecords(String token) {
        MutableLiveData<List<Record>> data = new MutableLiveData<>();
        RetrofitClient.getInstance().getApiService().getRecords(token)
            .enqueue(new Callback<List<Record>>() {
                @Override
                public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                    data.postValue(response.body());
                }
                @Override
                public void onFailure(Call<List<Record>> call, Throwable t) {
                    data.postValue(null);
                }
            });
        return data;
    }

    public LiveData<Boolean> uploadScanResult(String token, String code) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        RetrofitClient.getInstance().getApiService().uploadScanResult(token, code)
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    data.postValue(response.isSuccessful());
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    data.postValue(false);
                }
            });
        return data;
    }
} 