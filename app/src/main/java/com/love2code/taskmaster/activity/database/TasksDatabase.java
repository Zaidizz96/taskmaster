package com.love2code.taskmaster.activity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.love2code.taskmaster.activity.dao.TaskDao;
import com.love2code.taskmaster.activity.model.Task;

@Database(entities = {Task.class} , version = 1)
@TypeConverters({TaskDatabaseConverters.class})
public abstract class TasksDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

}
