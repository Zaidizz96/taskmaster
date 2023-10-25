package com.love2code.taskmaster.activity.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class TaskDatabaseConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null? null : new Date(value);
    }

    @TypeConverter
    public static Long fromTimestamp(Date date){
        return date == null? null : date.getTime();
    }
}
