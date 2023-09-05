package com.example.listit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.listit.adapters.DatabaseAdapter;
import com.example.listit.data.DataHolder;
import com.example.listit.data.TaskModel;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    TextView timeTV, timeChanger, date, dateChanger, cancel, save;
    EditText taskBodyED;

    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskBodyED = findViewById(R.id.taskBodyED);
        timeTV = findViewById(R.id.taskTime);
        timeChanger = findViewById(R.id.timeChanger);
        date = findViewById(R.id.taskDate);
        dateChanger = findViewById(R.id.dateChanger);
        cancel = findViewById(R.id.cancelButton);
        save = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("ID");

        databaseAdapter = new DatabaseAdapter(this);
        TaskModel task = databaseAdapter.getRow(taskId);

        taskBodyED.setText(task.getBody());
        timeTV.setText(task.getTime());
        date.setText(task.getDate());

        timeChanger.setOnClickListener(v -> timePicker());

        dateChanger.setOnClickListener(v -> datePicker());

        save.setOnClickListener(v -> {

            String body = taskBodyED.getText().toString();
            String time = timeTV.getText().toString();
            String taskDate = date.getText().toString();

            databaseAdapter.updateRow(task.getId(), body, time, taskDate);

            for (TaskModel modifiedTask : DataHolder.tasks) {
                if (modifiedTask.getId().equals(task.getId())) {

                    modifiedTask.setBody(body);
                    modifiedTask.setTime(time);
                    modifiedTask.setDate(taskDate);
                    finish();

                }
            }

        });

        cancel.setOnClickListener(v -> finish());

    }

    private void timePicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay % 12 == 0 ? 12 : hourOfDay % 12;
                String minutes = minute % 60 == 0 ? "00" : String.valueOf(minute % 60);
                String state = hourOfDay >= 12 ? "PM" : "AM";
                String time = String.format("%d:%s", hour, minutes);
                timeTV.setText(time + " " + state);
            }
        }, Calendar.HOUR, Calendar.MINUTE, false);

        timePickerDialog.show();

    }

    private void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                date.setText(String.format("%s/%s/%s", year, month, day));

            }
        }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

        // prevent past years
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

}

