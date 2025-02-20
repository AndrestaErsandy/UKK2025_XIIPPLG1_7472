package com.example.ukk2025_xiipplg1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TaskModel> taskList;
    private Context context;
    private static final String COMPLETE_TASK_URL = "http://172.16.0.106/ukk2025/completeTask.php";

    public TaskAdapter(Context context, List<TaskModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = taskList.get(position);

        holder.txtCategory.setText(task.getCategory());
        holder.txtTask.setText(task.getTask());
        holder.txtStatus.setText(task.getStatus());

        if ("complete".equals(task.getStatus())) {
            holder.imgComplete.setImageResource(R.drawable.ic_checked);
            holder.imgComplete.setEnabled(false);
        } else {
            holder.imgComplete.setImageResource(R.drawable.ic_check);
            holder.imgComplete.setEnabled(true);
        }

        holder.imgComplete.setOnClickListener(v -> markTaskComplete(task, holder));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtTask, txtStatus;
        ImageView imgComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.taskCategory);
            txtTask = itemView.findViewById(R.id.taskTitle);
            txtStatus = itemView.findViewById(R.id.taskStatus);
            imgComplete = itemView.findViewById(R.id.imgComplete);
        }
    }

    private void markTaskComplete(TaskModel task, ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                COMPLETE_TASK_URL,
                response -> {
                    Log.d("TaskAdapter", "Response: " + response);
                    if (response.equals("Task marked as 'complete'")) {
                        task.setStatus("complete");
                        holder.imgComplete.setImageResource(R.drawable.ic_checked);
                        holder.imgComplete.setEnabled(false);
                    }
                },
                error -> Log.e("TaskAdapter", "Volley Error: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("task_id", task.getId());
                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}
