package com.pensun.checkapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import com.pensun.checkapp.model.Record;

public class RecordRepository {
    public LiveData<List<Record>> getRecords() {
        MutableLiveData<List<Record>> data = new MutableLiveData<>();
        // TODO: 调用网络接口获取记录列表，填充data
        data.postValue(null); // 占位
        return data;
    }
} 