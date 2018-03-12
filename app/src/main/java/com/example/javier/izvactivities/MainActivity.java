package com.example.javier.izvactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView listViewActivities;
    private List<IzvActivity> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewActivities = findViewById(R.id.listViewActivities);
        listActivities();

        listViewActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.setAction("edit");
                IzvActivity activity = activityList.get(i);
                intent.putExtra("id", activity.getId());
                intent.putExtra("name", activity.getName());
                intent.putExtra("year", activity.getYear());
                intent.putExtra("month", activity.getMonth());
                intent.putExtra("day", activity.getDay());
                intent.putExtra("teachers", activity.getTeachers());
                intent.putExtra("groups", activity.getGroups());
                startActivityForResult(intent, 0);
            }
        });

        listViewActivities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                IzvActivity activity = activityList.get(i);
                intent.putExtra("id", activity.getId());
                intent.putExtra("name", activity.getName());
                startActivityForResult(intent, 0);
                return false;
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.setAction("add");
                startActivityForResult(intent, 0);
            }
        });
    }

    private void listActivities() {
        IzvActivitiesServiceRetrofit.getService().getActivities().enqueue(new Callback<List<IzvActivity>>() {
            @Override
            public void onResponse(Call<List<IzvActivity>> call, Response<List<IzvActivity>> response) {
                if (response.isSuccessful()) {
                    activityList = response.body();
                    showList(activityList);
                }
            }

            @Override
            public void onFailure(Call<List<IzvActivity>> call, Throwable t) {
                Log.i("myTag", "request failure");
            }
        });
    }

    private void showList(List<IzvActivity> activityList) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        for (IzvActivity activity : activityList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Name", activity.getName());
            String month = new DateFormatSymbols().getMonths()[activity.getMonth()];
            map.put("Date", month.substring(0, 3) + " " + activity.getDay());
            arrayList.add(map);
        }

        listViewActivities.setAdapter(new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Name", "Date"}, new int[]{android.R.id.text1, android.R.id.text2}));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            listActivities();
        }
    }
}
