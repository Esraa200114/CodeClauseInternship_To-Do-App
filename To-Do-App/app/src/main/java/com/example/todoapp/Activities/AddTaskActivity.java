package com.example.todoapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.Database.TasksDatabase;
import com.example.todoapp.Model.Task;
import com.example.todoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddTaskActivity extends AppCompatActivity {

    private EditText task_et;
    private TextView title_tv,  date_tv, time_tv;
    private Button pick_time_btn, pick_date_btn;
    private FloatingActionButton save_btn;
    private int d, m, y, new_hour, hour, minute;
    private Task task;
    private Calendar calendar1, calendar2;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePicker;
    private SimpleDateFormat simpleDateFormat;
    private String time;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(AddTaskActivity.this, R.color.white));

        title_tv = findViewById(R.id.title_tv);
        task_et = findViewById(R.id.task_et);
        date_tv = findViewById(R.id.task_date_tv);
        time_tv = findViewById(R.id.task_time_tv);
        pick_time_btn = findViewById(R.id.time_picker_btn);
        pick_date_btn = findViewById(R.id.date_picker_btn);
        save_btn = findViewById(R.id.save_btn);

        title_tv.setText(getIntent().getStringExtra("activity_title"));
        task_et.setText(getIntent().getStringExtra("task_"));
        date_tv.setText(getIntent().getStringExtra("date_"));
        time_tv.setText(getIntent().getStringExtra("time_"));

        pick_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });

        pick_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(title_tv.getText().toString().equals("Add Task")){
                    if(task_et.getText().toString().equals("") || date_tv.getText().toString().equals("") || time_tv.getText().toString().equals("")){
                        Toast.makeText(AddTaskActivity.this,"Please Fill Required Fields!",Toast.LENGTH_SHORT).show();
                    }else{
                        TasksDatabase.getInstance(getApplicationContext()).tasksDao().insertTask(new Task(0, task_et.getText().toString(), date_tv.getText().toString(), time_tv.getText().toString()));
                        Toast.makeText(AddTaskActivity.this,"New Task Added!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddTaskActivity.this, TasksListActivity.class));
                    }
                }else{

                    task = new Task();
                    task.setId(getIntent().getIntExtra("task_id", -1));
                    task.setStatus(getIntent().getIntExtra("task_status", -1));
                    task.setTask(task_et.getText().toString());
                    task.setDate(date_tv.getText().toString());
                    task.setTime(time_tv.getText().toString());

                    TasksDatabase.getInstance(getApplicationContext()).tasksDao().updateTask(task);
                    Toast.makeText(AddTaskActivity.this,"Task Updated Successfully!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddTaskActivity.this, TasksListActivity.class));
                }
            }
        });
    }

    private void openDatePicker() {

        calendar1 = Calendar.getInstance();
        d = calendar1.get((Calendar.DAY_OF_MONTH));
        m = calendar1.get((Calendar.MONTH));
        y = calendar1.get((Calendar.YEAR));

        datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date_tv.setText(FormatDate(month, dayOfMonth) + "/" + String.valueOf(year));
            }
        }, y, m, d);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void openTimePicker(){

        calendar1 = Calendar.getInstance();
        hour = calendar1.get(Calendar.HOUR_OF_DAY);
        minute = calendar1.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);
                calendar2.setTimeZone(TimeZone.getDefault());

                simpleDateFormat = new SimpleDateFormat("k:mm a");
                time = simpleDateFormat.format(calendar2.getTime());

                setFormattedTime(time);
            }
        },hour, minute, false);

        timePicker.show();
    }

    private String FormatDate(int month, int dayOfMonth){

        if(dayOfMonth < 10 && month + 1 < 10){
            return "0" + String.valueOf(dayOfMonth) + "/" + "0" + String.valueOf(month + 1);
        }
        else if (dayOfMonth < 10 && month + 1 > 10){
            return "0" + String.valueOf(dayOfMonth) + "/" +String.valueOf(month + 1);
        }
        return String.valueOf(dayOfMonth)+ "/" + "0" + String.valueOf(month + 1);
    }

    private void setFormattedTime(String unformattedTime){

        if(time.length() == 8){

            new_hour = Integer.parseInt(time.substring(0, 2));

            if(new_hour > 12){

                switch (new_hour){

                    case 13: new_hour = 1; break;
                    case 14: new_hour = 2; break;
                    case 15: new_hour = 3; break;
                    case 16: new_hour = 4; break;
                    case 17: new_hour = 5; break;
                    case 18: new_hour = 6; break;
                    case 19: new_hour = 7; break;
                    case 20: new_hour = 8; break;
                    case 21: new_hour = 9; break;
                    case 22: new_hour = 10; break;
                    case 23: new_hour = 11; break;
                    case 24: new_hour = 12; break;
                }
            }
            time_tv.setText(String.valueOf(new_hour) + time.substring(2, time.length()));
        }else{
            time_tv.setText(time);
        }
    }
}