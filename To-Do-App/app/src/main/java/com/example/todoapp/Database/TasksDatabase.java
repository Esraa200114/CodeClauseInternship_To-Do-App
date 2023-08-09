package com.example.todoapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todoapp.Model.Task;
import com.example.todoapp.Dao.TasksDao;

@Database(entities = {Task.class}, version = 3)
public abstract class TasksDatabase extends RoomDatabase {

    private static TasksDatabase instance;
    public abstract TasksDao tasksDao();

    public static synchronized TasksDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TasksDatabase.class, "tasks_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
