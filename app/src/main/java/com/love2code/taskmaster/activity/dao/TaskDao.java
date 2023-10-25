package com.love2code.taskmaster.activity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.love2code.taskmaster.activity.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void addTask(Task task);
    @Query("select * from Task")
    List<Task> findAll();
    @Query("select * from Task where id = :TaskId")
    Task getTaskById(long TaskId);
    @Query("select * from Task order by title ASC")
    List<Task> findAllSortedByName();
}
