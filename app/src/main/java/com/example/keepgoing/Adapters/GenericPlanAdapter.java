package com.example.keepgoing.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.keepgoing.Class.Exercise;
import com.example.keepgoing.Class.User;
import com.example.keepgoing.R;

import java.util.ArrayList;
import java.util.List;

public class GenericPlanAdapter extends RecyclerView.Adapter<GenericPlanAdapter.MyViewHolder> {
    private Context context;
    private List<Exercise> exercises;
    private List<Boolean> isClicked;
    private User user;
    public GenericPlanAdapter(Context context, List<Exercise> exercises, User user) {
        this.context = context;
        this.exercises = exercises;
        this.user = user;
        isClicked = new ArrayList<>();
        for(int i=0; i<exercises.size();i++)
            isClicked.add(false);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Exercise, Description, Sets;
        ImageView ExerciseImage, Arrow;
        ConstraintLayout constraintLayout;
        LinearLayout linearLayout;
        RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Exercise = itemView.findViewById(R.id.Exercise);
            Description = itemView.findViewById(R.id.Description);
            Sets = itemView.findViewById(R.id.Sets);
            ExerciseImage = itemView.findViewById(R.id.ExerciseImage);
            Arrow = itemView.findViewById(R.id.Arrow);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.exercise_view,parent,false);
        return new MyViewHolder(view);
    }
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Exercise exercise = exercises.get(position);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.Exercise.setText(exercise.getMuscleType() + ": " +exercise.getExercise());
        if(!exercise.getDescription().equals(""))
            holder.Description.setText(context.getResources().getString(R.string.Description) + ": " + exercise.getDescription());
        if(!exercise.getImage().equals("")) {
            Glide.with(context).asBitmap().load(exercise.getImage()).into(new CustomTarget<Bitmap>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) { holder.ExerciseImage.setBackground(new BitmapDrawable(context.getResources(), resource)); }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) { }
            });
        } else{
            holder.ExerciseImage.setImageResource(R.drawable.camera);
        }
        holder.Sets.setText(context.getResources().getString(R.string.Sets) + ": " + exercise.getSets().size());
        holder.constraintLayout.setOnClickListener( v -> {
            isClicked.set(position,!isClicked.get(position));
            if(isClicked.get(position)) {
                holder.Arrow.setImageResource(R.drawable.arrow_up);
                holder.linearLayout.setVisibility(View.VISIBLE);
                SetsAdapter setsAdapter = new SetsAdapter(context, exercises.get(position).getSets());
                holder.recyclerView.setAdapter(setsAdapter);
            }
            else{
                holder.Arrow.setImageResource(R.drawable.arrow_down);
                holder.linearLayout.setVisibility(View.GONE);
            }
        });
    }
    public int getItemCount() { return exercises.size(); }
}
