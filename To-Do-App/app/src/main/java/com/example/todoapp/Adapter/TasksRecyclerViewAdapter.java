package com.example.todoapp.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Activities.AddTaskActivity;
import com.example.todoapp.Model.Task;
import com.example.todoapp.R;
import com.example.todoapp.Database.TasksDatabase;
import com.example.todoapp.Activities.TasksListActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {

    private List<Task> taskList;
    private TasksListActivity tasksList_activity;
    private Intent intent;
    private Date currentDate;
    private SimpleDateFormat dateFormat;
    private String formattedDate;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    public static boolean flag = false;

    public TasksRecyclerViewAdapter(TasksListActivity tasksList_activity) {
        this.tasksList_activity = tasksList_activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        Task taskItem = taskList.get(position);

        holder.task_txt.setText(taskItem.getTask());

        if(taskItem.getStatus() != 0){
            holder.task.setChecked(true);
            holder.task_txt.setPaintFlags(holder.task_txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.task.setChecked(false);
        }

        if(taskItem.getDate().equals(getCurrentDate())){
            holder.date.setText("Due Today");
            holder.date.setTextColor(Color.RED);
        }else{
            holder.date.setText(taskItem.getDate());
            holder.date.setTextColor(Color.BLACK);
        }

        holder.time.setText(taskItem.getTime());

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.task_txt.setPaintFlags(holder.task_txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskItem.setStatus(1);
                } else {
                    holder.task_txt.setPaintFlags(holder.task_txt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    taskItem.setStatus(0);
                }
                TasksDatabase.getInstance(holder.task.getContext()).tasksDao().updateTask(taskItem);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(tasksList_activity, AddTaskActivity.class);
                intent.putExtra("activity_title", "Edit Task");
                intent.putExtra("task_id", taskItem.getId());
                intent.putExtra("task_status", taskItem.getStatus());
                intent.putExtra("task_", taskItem.getTask());
                intent.putExtra("date_", taskItem.getDate());
                intent.putExtra("time_", taskItem.getTime());
                holder.edit.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder = new AlertDialog.Builder(holder.delete.getContext(), R.style.CustomAlertDialog);
                builder.setTitle("Delete Task");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Are you sure you want to delete this task ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TasksDatabase.getInstance(holder.delete.getContext()).tasksDao().deleteTaskByID(taskItem.getId());
                        taskList.remove(position);
                        notifyDataSetChanged();
                        checkIfTaskListIsEmpty();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public String getCurrentDate(){
        currentDate = new Date();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    public void checkIfTaskListIsEmpty() {
        if (taskList.isEmpty()) {
            tasksList_activity.showEmptyMessage();
        }
    }

    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox task;
        TextView task_txt, date, time;
        Button edit, delete;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.task_cb);
            task_txt = view.findViewById(R.id.task_tv);
            date = view.findViewById(R.id.date_tv);
            time = view.findViewById(R.id.time_tv);
            edit = view.findViewById(R.id.edit_btn);
            delete = view.findViewById(R.id.delete_btn);
        }
    }
}
