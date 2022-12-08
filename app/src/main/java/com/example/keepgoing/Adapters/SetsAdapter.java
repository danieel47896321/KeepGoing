package com.example.keepgoing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepgoing.Class.ExerciseSet;
import com.example.keepgoing.R;

import java.util.List;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.MyViewHolder> {
    private Context context;
    private List<ExerciseSet> exerciseSets;
    public SetsAdapter(Context context, List<ExerciseSet> exerciseSets) {
        this.context = context;
        this.exerciseSets = exerciseSets;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Sets, Reps, Weight, Rest;
        ImageView CheckCircle, PlayCircle;
        NumberPicker RepsNumberPicker, RestNumberPicker, WeightNumberPicker;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Sets = itemView.findViewById(R.id.Sets);
            Reps = itemView.findViewById(R.id.Reps);
            Weight = itemView.findViewById(R.id.Weight);
            Rest = itemView.findViewById(R.id.Rest);
            CheckCircle = itemView.findViewById(R.id.CheckCircle);
            PlayCircle = itemView.findViewById(R.id.PlayCircle);
            RepsNumberPicker = itemView.findViewById(R.id.RepsNumberPicker);
            RepsNumberPicker.setMinValue(0);
            RepsNumberPicker.setMaxValue(300);
            RestNumberPicker = itemView.findViewById(R.id.RestNumberPicker);
            RestNumberPicker.setMinValue(0);
            RestNumberPicker.setMaxValue(300);
            WeightNumberPicker = itemView.findViewById(R.id.WeightNumberPicker);
            WeightNumberPicker.setMinValue(0);
            WeightNumberPicker.setMaxValue(300);
        }
    }
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.set_view,parent,false);
        return new MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExerciseSet exerciseSet = exerciseSets.get(position);
        holder.RepsNumberPicker.setValue(exerciseSet.getReps());
        holder.RestNumberPicker.setValue(exerciseSet.getRest());
        holder.WeightNumberPicker.setValue(exerciseSet.getWeight());
        holder.Sets.setText((exerciseSet.getSetNumber()+1)+"");
        holder.Reps.setText(context.getResources().getString(R.string.Reps) + ":");
        holder.Weight.setText(context.getResources().getString(R.string.Weight) + ":");
        holder.Rest.setText(context.getResources().getString(R.string.Rest) + ":");
        holder.CheckCircle.setImageResource(R.drawable.check_circle);
        holder.PlayCircle.setImageResource(R.drawable.play);
        if(exerciseSet.isFinish())
            holder.CheckCircle.setImageResource(R.drawable.finish_check_circle);
        else
            holder.CheckCircle.setImageResource(R.drawable.check_circle);
        holder.CheckCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseSet.setFinish(!exerciseSet.isFinish());
                if(exerciseSet.isFinish())
                    holder.CheckCircle.setImageResource(R.drawable.finish_check_circle);
                else
                    holder.CheckCircle.setImageResource(R.drawable.check_circle);
            }
        });
        holder.PlayCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseSet.setPlay(!exerciseSet.isPlay());
                if(exerciseSet.isPlay())
                    holder.PlayCircle.setImageResource(R.drawable.stop_circle);
                else
                    holder.PlayCircle.setImageResource(R.drawable.play);
            }
        });
    }
    public int getItemCount() { return exerciseSets.size(); }
}
