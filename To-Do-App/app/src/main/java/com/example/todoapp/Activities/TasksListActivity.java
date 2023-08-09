package com.example.todoapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.Adapter.TasksRecyclerViewAdapter;
import com.example.todoapp.Database.TasksDatabase;
import com.example.todoapp.Model.Task;
import com.example.todoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TasksListActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerview;
    private TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    private FloatingActionButton add_task_fab;
    public static TasksDatabase tasksDatabase;
    private List<Task> taskList;
    private TextView message;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(TasksListActivity.this, R.color.white));

        add_task_fab = findViewById(R.id.add_task_btn);
        message = findViewById(R.id.message_tv);

        tasksRecyclerview = findViewById(R.id.tasks_rv);
        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter(this);
        tasksRecyclerview.setAdapter(tasksRecyclerViewAdapter);

        taskList = tasksDatabase.getInstance(getApplicationContext()).tasksDao().getTasks();

        if(taskList.size() > 0){
            tasksRecyclerViewAdapter.setTasks(taskList);
        }else{
            showEmptyMessage();
        }

        add_task_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(TasksListActivity.this, AddTaskActivity.class);
                intent.putExtra("activity_title", "Add Task");
                intent.putExtra("task_", "");
                intent.putExtra("date_", "");
                intent.putExtra("time_", "");
                startActivity(intent);
            }
        });
    }

    public void showEmptyMessage() {
        tasksRecyclerview.setVisibility(View.GONE);
        message.setText("All tasks completed! \uD83D\uDC4F" + "\n" + "Enjoy your free time ⏳ or" + "\n" + "Add new tasks to stay productive \uD83D\uDCCB");
        message.setVisibility(View.VISIBLE);
    }

}