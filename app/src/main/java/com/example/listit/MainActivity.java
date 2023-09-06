package com.example.listit;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.adapters.DatabaseAdapter;
import com.example.listit.adapters.TodaySectionRecyclerViewAdapter;
import com.example.listit.data.DataHolder;
import com.example.listit.data.TaskModel;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends Activity implements TaskSelector {

    TextView userNameTV, phraseTV, greetingTV;
    Button addTaskButton, setTimeButton, homeButton;
    EditText taskNameED;
    RecyclerView todayRecyclerView;

    SharedPreferences sharedPreferences;
    Intent intentForSettingName;

    TodaySectionRecyclerViewAdapter todaySectionRecyclerViewAdapter;
    DatabaseAdapter databaseAdapter;

    int hour, taskPosition;

    String minutes;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameTV = findViewById(R.id.userNameTV);
        greetingTV = findViewById(R.id.greeting);
        phraseTV = findViewById(R.id.phrase);
        addTaskButton = findViewById(R.id.taskBTN);
        setTimeButton = findViewById(R.id.setTimeButton);
        taskNameED = findViewById(R.id.taskED);
        homeButton = findViewById(R.id.homeButton);
        todayRecyclerView = findViewById(R.id.todayRecyclerView);

        sharedPreferences = getSharedPreferences("UserName", Context.MODE_PRIVATE);

        firstTimeCheck();

        userNameTV.setText(sharedPreferences.getString("NAME", "NotFound"));

        //

        databaseAdapter = new DatabaseAdapter(this);

        emptyListCheck();

        todaySectionRecyclerViewAdapter = new TodaySectionRecyclerViewAdapter(this);
        todayRecyclerView.setAdapter(todaySectionRecyclerViewAdapter);
        swipeToDelete();

        addTaskButton.setOnClickListener(v -> {

            String taskName = taskNameED.getText().toString();
            String time = setTimeButton.getText().toString();

            if (!taskName.isEmpty() && !time.equals("Set time")) {
                TaskModel newTask = new TaskModel(taskName, time, "Today");
                newTask.setId(String.valueOf(databaseAdapter.insertRow(newTask)));
                DataHolder.tasks.add(newTask);
                todaySectionRecyclerViewAdapter.notifyDataSetChanged();
                taskNameED.setText("");
                taskNameED.clearFocus();
                setTimeButton.setText(getResources().getString(R.string.set_time));
            }

            emptyListCheck();

        });

        setTimeButton.setOnClickListener(v -> timePicker());

        homeButton.setOnClickListener(v -> {});

    }

    private void timePicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay % 12 == 0 ? 12 : hourOfDay % 12;
                minutes = minute % 60 == 0 ? "00" : String.valueOf(minute % 60);
                String state = hourOfDay >= 12 ? "PM" : "AM";
                String time = String.format("%d:%s", hour, minutes);
                setTimeButton.setText(time + " " + state);

            }
        }, Calendar.HOUR, Calendar.MINUTE, false);

        timePickerDialog.show();

    }

    private void firstTimeCheck() {
        File f = new File("/data/data/com.example.listit/shared_prefs/UserName.xml");
        if (!f.exists()) {
            intentForSettingName = new Intent(this, SecondActivity.class);
            startActivity(intentForSettingName);
            finish();
        }
    }

    private void swipeToDelete() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                TaskModel task = DataHolder.tasks.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();

                DataHolder.tasks.remove(position);
                databaseAdapter.remove(task.getId());
                todaySectionRecyclerViewAdapter.notifyItemRemoved(position);
                emptyListCheck();
            }
        }).attachToRecyclerView(todayRecyclerView);

    }

    private void emptyListCheck() {
        if (DataHolder.tasks.size() > 0) {
            userNameTV.setVisibility(View.INVISIBLE);
            greetingTV.setVisibility(View.INVISIBLE);
            phraseTV.setVisibility(View.INVISIBLE);
        } else {
            userNameTV.setVisibility(View.VISIBLE);
            greetingTV.setVisibility(View.VISIBLE);
            phraseTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("ID", DataHolder.tasks.get(position).getId());
        startActivity(intent);
        taskPosition = position;
    }

    @Override
    protected void onResume() {
        super.onResume();
        todaySectionRecyclerViewAdapter.notifyItemChanged(taskPosition);
    }

}