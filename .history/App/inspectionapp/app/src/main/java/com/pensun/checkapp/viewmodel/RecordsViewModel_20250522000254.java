package com.pensun.checkapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import com.pensun.checkapp.model.Record;
import com.pensun.checkapp.repository.RecordRepository;

public class RecordsViewModel extends ViewModel {
    private final RecordRepository recordRepository = new RecordRepository();
    public LiveData<List<Record>> getRecords() {
        return recordRepository.getRecords();
    }
} 