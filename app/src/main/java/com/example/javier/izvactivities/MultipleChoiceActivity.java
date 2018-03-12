package com.example.javier.izvactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultipleChoiceActivity extends AppCompatActivity {
    private String action;
    private String[] strings;
    private ListView listViewMultipleChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        action = getIntent().getAction();
        assert action != null;
        setTitle("Select " + action);
        listViewMultipleChoice = findViewById(R.id.listViewMultipleChoice);

        switch (action) {
            case "teachers":
                IzvActivitiesServiceRetrofit.getService().getTeachers().enqueue(new Callback<String[]>() {
                    @Override
                    public void onResponse(Call<String[]> call, Response<String[]> response) {
                        if (response.isSuccessful()) {
                            strings = response.body();
                            showList();
                        }
                    }

                    @Override
                    public void onFailure(Call<String[]> call, Throwable t) {
                        Log.i("myTag", "request failure");
                    }
                });
                break;
            case "groups":
                IzvActivitiesServiceRetrofit.getService().getGroups().enqueue(new Callback<String[]>() {
                    @Override
                    public void onResponse(Call<String[]> call, Response<String[]> response) {
                        if (response.isSuccessful()) {
                            strings = response.body();
                            showList();
                        }
                    }

                    @Override
                    public void onFailure(Call<String[]> call, Throwable t) {
                        Log.i("myTag", "request failure");
                    }
                });
                break;
        }
    }

    private void showList() {
        listViewMultipleChoice.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, strings));

        String[] selectedItems = new String[0];

        switch (action) {
            case "teachers":
                selectedItems = getIntent().getStringArrayExtra("selectedTeachers");
                break;
            case "groups":
                selectedItems = getIntent().getStringArrayExtra("selectedGroups");
                break;
        }

        for (String selectedItem : selectedItems) {
            int position = Arrays.asList(strings).indexOf(selectedItem);

            if (position != -1) {
                listViewMultipleChoice.setItemChecked(position, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multiple_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.multipleChoiceDone) {
            SparseBooleanArray checkedItemPositions = listViewMultipleChoice.getCheckedItemPositions();
            ArrayList<String> list = new ArrayList<>();

            for (int i = 0; i < checkedItemPositions.size(); i++) {
                if (checkedItemPositions.valueAt(i)) {
                    list.add(strings[checkedItemPositions.keyAt(i)]);
                }
            }

            if (list.isEmpty()) {
                switch (action) {
                    case "teachers":
                        Toast.makeText(this, "Select at least one teacher", Toast.LENGTH_LONG).show();
                        break;
                    case "groups":
                        Toast.makeText(this, "Select at least one group", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }

            Intent intent = new Intent();
            intent.putExtra(action, list.toArray(new String[list.size()]));
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
