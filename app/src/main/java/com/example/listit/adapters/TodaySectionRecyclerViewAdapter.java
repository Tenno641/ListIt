package com.example.listit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.R;
import com.example.listit.TaskSelector;
import com.example.listit.data.TaskModel;

import java.util.List;

public class TodaySectionRecyclerViewAdapter extends RecyclerView.Adapter<TodaySectionRecyclerViewAdapter.ViewHolder> {

    List<TaskModel> taskModels;
    TaskSelector taskSelector;

    public TodaySectionRecyclerViewAdapter(List<TaskModel> taskModels, TaskSelector taskSelector) {
        this.taskModels = taskModels;
        this.taskSelector = taskSelector;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_task, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskModel model = taskModels.get(position);

        holder.body.setText(model.getBody());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());

    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView body, time, date;
        CardView taskCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            body = itemView.findViewById(R.id.taskBody);
            time = itemView.findViewById(R.id.taskTime);
            date = itemView.findViewById(R.id.taskDate);
            taskCardView = itemView.findViewById(R.id.taskCardView);

            itemView.setOnClickListener(v -> taskSelector.onItemClick(getAdapterPosition()));


        }
    }

}
