package com.example.javier.izvactivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    private static final int REQUEST_TEACHERS = 0;
    private static final int REQUEST_GROUPS = 1;
    private final Calendar calendar = Calendar.getInstance();
    private String action;
    private int year;
    private int month;
    private int day;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextDate;
    private TextInputEditText textInputEditTextTeachers;
    private TextInputEditText textInputEditTextGroups;
    private String[] teachers;
    private String[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        action = getIntent().getAction();
        assert action != null;
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextDate = findViewById(R.id.textInputEditTextDate);
        textInputEditTextTeachers = findViewById(R.id.textInputEditTextTeachers);
        textInputEditTextGroups = findViewById(R.id.textInputEditTextGroups);

        switch (action) {
            case "add":
                setTitle("Add activity");
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                teachers = new String[0];
                groups = new String[0];
                break;
            case "edit":
                setTitle("Edit activity");
                textInputEditTextName.setText(getIntent().getStringExtra("name"));
                year = getIntent().getIntExtra("year", 0);
                month = getIntent().getIntExtra("month", 0);
                day = getIntent().getIntExtra("day", 0);
                teachers = getIntent().getStringArrayExtra("teachers");
                updateTeachersText();
                groups = getIntent().getStringArrayExtra("groups");
                updateGroupsText();
                break;
        }

        updateDateText();
        textInputEditTextDate.setKeyListener(null);

        textInputEditTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });

        textInputEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        textInputEditTextTeachers.setKeyListener(null);

        textInputEditTextTeachers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    startTeachersActivity();
                }
            }
        });

        textInputEditTextTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTeachersActivity();
            }
        });

        textInputEditTextGroups.setKeyListener(null);

        textInputEditTextGroups.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    startGroupsActivity();
                }
            }
        });

        textInputEditTextGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGroupsActivity();
            }
        });
    }

    private void updateTeachersText() {
        String s = Arrays.toString(teachers).replace("[", "").replace("]", "");
        textInputEditTextTeachers.setText(s);
    }

    private void updateGroupsText() {
        String s = Arrays.toString(groups).replace("[", "").replace("]", "");
        textInputEditTextGroups.setText(s);
    }

    private void updateDateText() {
        String monthName = new DateFormatSymbols().getMonths()[month];
        String date = monthName + " " + String.valueOf(day) + ", " + String.valueOf(year);
        textInputEditTextDate.setText(date);
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearSet, int monthSet, int daySet) {
                year = yearSet;
                month = monthSet;
                day = daySet;
                updateDateText();
            }
        }, year, month, day);

        dialog.show();
    }

    private void startTeachersActivity() {
        Intent intent = new Intent(EditActivity.this, MultipleChoiceActivity.class);
        intent.setAction("teachers");
        intent.putExtra("selectedTeachers", teachers);
        startActivityForResult(intent, REQUEST_TEACHERS);
    }

    private void startGroupsActivity() {
        Intent intent = new Intent(EditActivity.this, MultipleChoiceActivity.class);
        intent.setAction("groups");
        intent.putExtra("selectedGroups", groups);
        startActivityForResult(intent, REQUEST_GROUPS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TEACHERS) {
                teachers = data.getStringArrayExtra("teachers");
                updateTeachersText();
            } else if (requestCode == REQUEST_GROUPS) {
                groups = data.getStringArrayExtra("groups");
                updateGroupsText();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editSave) {
            String name = String.valueOf(textInputEditTextName.getText());
            TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);

            if (name.isEmpty()) {
                textInputLayoutName.setError("Name required");
                return false;
            }

            TextInputLayout textInputLayoutTeachers = findViewById(R.id.textInputLayoutTeachers);

            if (teachers.length == 0) {
                textInputLayoutTeachers.setError("At least one teacher required");
                return false;
            }

            TextInputLayout textInputLayoutGroups = findViewById(R.id.textInputLayoutGroups);

            if (groups.length == 0) {
                textInputLayoutGroups.setError("At least one group required");
                return false;
            }

            IzvActivity activity = new IzvActivity(name, year, month, day, teachers, groups);

            switch (action) {
                case "add":
                    IzvActivitiesServiceRetrofit.getService().addActivity(activity).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("myTag", "request failure");
                        }
                    });
                    break;
                case "edit":
                    int activityId = getIntent().getIntExtra("id", 0);

                    IzvActivitiesServiceRetrofit.getService().editActivity(activityId, activity).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("myTag", "request failure");
                        }
                    });
                    break;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
