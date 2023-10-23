package com.love2code.taskmaster.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.MainActivity;
import com.love2code.taskmaster.activity.TaskDetails;
import com.love2code.taskmaster.activity.model.Task;

import java.util.List;

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
        String taskTitle = tasks.get(position).getTitle();
        String taskBody = tasks.get(position).getBody();
        taskFragmentTextView.setText(taskTitle);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetailsPage = new Intent(callingActivity , TaskDetails.class);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_TITLE_EXTRA_TAG ,taskTitle);
            goToTaskDetailsPage.putExtra(MainActivity.TASK_BODY_EXTRA_TAG , taskBody);
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
