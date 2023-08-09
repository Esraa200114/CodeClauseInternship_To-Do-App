package com.example.todoapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.Model.Task;

import java.util.List;

@Dao
public interface TasksDao {

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("delete from tasks_table where id = :task_id")
    void deleteTaskByID(int task_id);

    @Query("select * from tasks_table")
    List<Task> getTasks();
}
