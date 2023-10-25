package com.love2code.taskmaster.activity.model;

import androidx.annotation.NonNull;

public enum TaskState {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In_progress"),
    COMPLETE("Complete");
    private final String taskStateText;

    TaskState(String taskStateText) {
        this.taskStateText = taskStateText;
    }

    public String getTaskStateText() {
        return taskStateText;
    }

    public static TaskState fromString(String text){
        for (TaskState task  :  TaskState.values() ) {
            if (task.taskStateText.equalsIgnoreCase(text)){
                return task;
            }
        }
        return null;
    }
    @NonNull
    @Override
    public String toString(){
        if(taskStateText == null){
            return "";
        }
        return taskStateText;
    }

}
