package com.pensun.checkapp.ui.records;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.pensun.checkapp.R;
import com.pensun.checkapp.model.Record;
import com.pensun.checkapp.viewmodel.RecordsViewModel;
import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    private RecordsViewModel recordsViewModel;
    private ListView listView;
    private TextView tvTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        listView = findViewById(R.id.lv_records);
        tvTip = findViewById(R.id.tv_records_tip);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String token = sp.getString("token", "");
        recordsViewModel.getRecords(token).observe(this, records -> {
            if (records == null || records.isEmpty()) {
                tvTip.setText("暂无巡检记录");
                listView.setAdapter(null);
            } else {
                tvTip.setText("");
                List<String> titles = new ArrayList<>();
                for (Record r : records) {
                    titles.add(r.getTitle() + " - " + r.getTime() + " - " + r.getStatus());
                }
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));
            }
        });
    }
} 