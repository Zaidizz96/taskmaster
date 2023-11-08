package com.love2code.taskmaster.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.MainActivity;
import com.love2code.taskmaster.activity.TaskDetails;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomePageRecyclerViewAdapter extends RecyclerView.Adapter<HomePageRecyclerViewAdapter.TasksViewHolder> {

    List<Task> tasks;
    Context callingActivity;

    public HomePageRecyclerViewAdapter(List<Task> tasks , Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_page, parent , false);
        return new TasksViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.textViewFragment);

        // Setup the date format (start)
        DateFormat dateCreatedIso8061InputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateCreatedIso8061InputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        @SuppressLint("SimpleDateFormat") DateFormat dateCreatedOutputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateCreatedOutputFormat.setTimeZone(TimeZone.getDefault());
        String dateCreatedString = "";

        try {
            {
                Date dateCreatedJavaDate = dateCreatedIso8061InputFormat.parse(tasks.get(position).getDateCreated().format());
                if (dateCreatedJavaDate != null){
                    dateCreatedString = dateCreatedOutputFormat.format(dateCreatedJavaDate);
                }
            }
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
        //Setup the date format (end)

        String taskTitle = tasks.get(position).getTitle();
        String taskBody = tasks.get(position).getBody();
        String taskState = tasks.get(position).getState().toString();
        String finalDateCreatedString = dateCreatedString;

        taskFragmentTextView.setText(taskTitle);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetailsPage = new Intent(callingActivity , TaskDetails.class);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_TITLE_EXTRA_TAG ,taskTitle);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_BODY_EXTRA_TAG , taskBody);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_STATE_EXTRA_TAG , taskState);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_DATE_EXTRA_TAG , finalDateCreatedString);
            callingActivity.startActivity(goToTaskDetailsPage);
        });


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
