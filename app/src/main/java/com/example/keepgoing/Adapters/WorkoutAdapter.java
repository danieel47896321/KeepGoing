package com.example.keepgoing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.keepgoing.Class.Plan;
import com.example.keepgoing.Class.User;
import com.example.keepgoing.R;
import com.example.keepgoing.User.GenericPlan;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder> {
    private Context context;
    private List<Plan> planList;
    private User user;
    private Intent intent;
    public WorkoutAdapter(Context context, List<Plan> planList, User user) {
        this.context = context;
        this.planList = planList;
        this.user = user;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView PlanName;
        TextView Date;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PlanName = itemView.findViewById(R.id.PlanName);
            Date = itemView.findViewById(R.id.Date);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.plan_view,parent,false);
        return new MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.PlanName.setText(plan.getPlanName());
        holder.Date.setText(plan.getDate());
        holder.constraintLayout.setOnClickListener( v -> {
            intent = new Intent(context, GenericPlan.class);
            intent.putExtra("user", user);
            intent.putExtra("plan", plan);
            context.startActivity(intent);
            ((Activity) context).finish();
        });
    }
    public int getItemCount() { return planList.size(); }
}