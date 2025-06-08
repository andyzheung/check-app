package com.pensun.inspection_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class RecordListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<Record> recordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
        fetchRecords();
    }

    private void fetchRecords() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://your-server/api/v1/records").newBuilder();
        urlBuilder.addQueryParameter("page", "1");
        urlBuilder.addQueryParameter("size", "10");
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject data = obj.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    recordList.clear();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject rec = list.getJSONObject(i);
                        Record r = new Record();
                        r.id = rec.optLong("id");
                        r.areaName = rec.optString("areaName");
                        r.inspectorName = rec.optString("inspectorName");
                        r.status = rec.optString("status");
                        r.inspectionTime = rec.optString("inspectionTime");
                        r.remark = rec.optString("remark");
                        recordList.add(r);
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } catch (Exception e) { }
            }
        });
    }

    // Record、RecordAdapter 需自行实现
} 