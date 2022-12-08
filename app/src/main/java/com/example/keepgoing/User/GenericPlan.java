package com.example.keepgoing.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keepgoing.Adapters.GenericPlanAdapter;
import com.example.keepgoing.Class.Exercise;
import com.example.keepgoing.Class.Plan;
import com.example.keepgoing.Class.User;
import com.example.keepgoing.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenericPlan extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView BackIcon;
    private TextView Title;
    private FloatingActionButton floatingActionButtonOpen;
    private ExtendedFloatingActionButton floatingActionButtonAdd, floatingActionButtonRemove;
    private Animation rotateOpen, rotateClose, toBottom, fromBottom;
    private Boolean isOpen = false;
    private Intent intent;
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Button ButtonAdd, ButtonRemove, ButtonCancel;
    private TextInputLayout TextInputLayoutExerciseName, TextInputLayoutDescription, TextInputLayoutTypeOfMuscle, TextInputLayoutReps,TextInputLayoutPlan;
    private Plan plan;
    private Context context;
    private User user = new User();
    private ArrayList<Exercise> exercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_plan);
        init();
    }
    private void init(){
        setID();
        BackIcon();
        setExercises();
        setAddAndRemove();
    }
    private void setID(){
        context = this.getBaseContext();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        exercises = new ArrayList<>();
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        plan = (Plan) intent.getSerializableExtra("plan");
        BackIcon = findViewById(R.id.BackIcon);
        Title = findViewById(R.id.Title);
        Title.setText(plan.getPlanName());
        floatingActionButtonOpen = findViewById(R.id.floatingActionButtonOpen);
        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonRemove = findViewById(R.id.floatingActionButtonRemove);
    }
    private void setAddAndRemove(){
        floatingActionButtonOpen.setOnClickListener( v -> {
            isOpen = !isOpen;
            rotateOpen = AnimationUtils.loadAnimation(context,R.anim.rotate_open);
            rotateClose = AnimationUtils.loadAnimation(context,R.anim.rotate_close);
            fromBottom = AnimationUtils.loadAnimation(context,R.anim.from_bottom);
            toBottom = AnimationUtils.loadAnimation(context,R.anim.to_bottom);
            if (isOpen) {
                floatingActionButtonAdd.setVisibility(View.VISIBLE);
                floatingActionButtonRemove.setVisibility(View.VISIBLE);
                floatingActionButtonAdd.setAnimation(fromBottom);
                floatingActionButtonRemove.setAnimation(fromBottom);
                floatingActionButtonOpen.setAnimation(rotateOpen);
                floatingActionButtonAdd.setClickable(true);
                floatingActionButtonRemove.setClickable(true);
                floatingActionButtonAdd.setOnClickListener( v1 -> AddExerciseDialog() );
                floatingActionButtonRemove.setOnClickListener( v1 -> RemoveExerciseDialog() );
            } else {
                floatingActionButtonAdd.setVisibility(View.INVISIBLE);
                floatingActionButtonRemove.setVisibility(View.INVISIBLE);
                floatingActionButtonAdd.setAnimation(toBottom);
                floatingActionButtonRemove.setAnimation(toBottom);
                floatingActionButtonOpen.setAnimation(rotateClose);
                floatingActionButtonAdd.setClickable(false);
                floatingActionButtonRemove.setClickable(false);
            }
        });
    }
    private void setExercises(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(plan.getDay()).child(user.getUid()).child(plan.getDate()).child("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercises.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                }
                GenericPlanAdapter genericPlan = new GenericPlanAdapter(context, exercises, user);
                recyclerView.setAdapter(genericPlan);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private void AddExerciseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutExerciseName = dialogView.findViewById(R.id.TextInputLayoutExerciseName);
        TextInputLayoutDescription = dialogView.findViewById(R.id.TextInputLayoutDescription);
        TextInputLayoutTypeOfMuscle = dialogView.findViewById(R.id.TextInputLayoutTypeOfMuscle);
        TextInputLayoutReps = dialogView.findViewById(R.id.TextInputLayoutReps);
        ButtonAdd = dialogView.findViewById(R.id.ButtonAdd);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MuscleType();
        ButtonCancel.setOnClickListener( v -> alertDialog.cancel() );
        ButtonAdd.setOnClickListener( v -> {
            if(TextInputLayoutExerciseName.getEditText().getText().toString().equals(""))
                TextInputLayoutExerciseName.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutExerciseName.setHelperText("");
            if(TextInputLayoutTypeOfMuscle.getEditText().getText().toString().equals(""))
                TextInputLayoutTypeOfMuscle.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutTypeOfMuscle.setHelperText("");
            if(TextInputLayoutReps.getEditText().getText().toString().equals(""))
                TextInputLayoutReps.setHelperText(getResources().getString(R.string.Required));
            else if(Integer.valueOf(TextInputLayoutReps.getEditText().getText().toString()) <= 0)
                TextInputLayoutReps.setHelperText(getResources().getString(R.string.NumberOfReps));
            else
                TextInputLayoutReps.setHelperText("");
            if(!(TextInputLayoutExerciseName.getEditText().getText().toString().equals("")) && (Integer.valueOf(TextInputLayoutReps.getEditText().getText().toString()) > 0) && !(TextInputLayoutTypeOfMuscle.getEditText().getText().toString().equals("")) && !(TextInputLayoutReps.getEditText().getText().toString().equals(""))){
                alertDialog.cancel();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(plan.getDay()).child(user.getUid()).child(plan.getDate()).child("exercises");
                exercises.add(new Exercise(TextInputLayoutExerciseName.getEditText().getText().toString(),TextInputLayoutDescription.getEditText().getText().toString(),TextInputLayoutTypeOfMuscle.getEditText().getText().toString(),"null",Integer.valueOf(TextInputLayoutReps.getEditText().getText().toString())));
                databaseReference.setValue(exercises);
            }
        });
    }
    private void MuscleType(){
        TextInputLayoutTypeOfMuscle.getEditText().setOnClickListener( v -> setDialog(getResources().getStringArray(R.array.MuscleType) ,getResources().getString(R.string.TypeOfMuscle),TextInputLayoutTypeOfMuscle.getEditText()) );
    }
    private void RemoveExerciseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_remove,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutPlan = dialogView.findViewById(R.id.TextInputLayoutPlan);
        ButtonRemove = dialogView.findViewById(R.id.ButtonRemove);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ExercisePick();
        ButtonCancel.setOnClickListener( v -> alertDialog.cancel() );
        ButtonRemove.setOnClickListener( v -> {
            if(TextInputLayoutPlan.getEditText().getText().toString().equals(""))
                TextInputLayoutPlan.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutPlan.setHelperText("");
            if(!TextInputLayoutPlan.getEditText().getText().toString().equals("")) {
                alertDialog.cancel();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(plan.getDay()).child(user.getUid()).child(plan.getDate()).child("exercises");
                for(int i=0; i<exercises.size();i++)
                    if(TextInputLayoutPlan.getEditText().getText().toString().equals(exercises.get(i).getExercise()))
                        exercises.remove(i);
                databaseReference.setValue(exercises);
            }
        });
    }
    private void ExercisePick(){
        TextInputLayoutPlan.getEditText().setOnClickListener( v -> {
            String Remove_plans[] = new String[exercises.size()];
            for(int i=0; i<exercises.size();i++)
                Remove_plans[i] = exercises.get(i).getExercise();
            setDialog(Remove_plans,getResources().getString(R.string.RemoveExercise), TextInputLayoutPlan.getEditText());
        });
    }
    private void setDialog(String[] array, String title, TextView textViewPick){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_item, array);
        ListViewSearch.setAdapter(adapter);
        EditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                textViewPick.setText(adapterView.getItemAtPosition(i).toString());
            }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener( v -> onBackPressed() );
    }
    public void onBackPressed(){
        intent = new Intent(GenericPlan.this, Home.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }
}