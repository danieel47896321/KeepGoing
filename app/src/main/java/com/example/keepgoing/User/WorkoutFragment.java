package com.example.keepgoing.User;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keepgoing.Adapters.WorkoutAdapter;
import com.example.keepgoing.Class.Plan;
import com.example.keepgoing.Class.PopUpMSG;
import com.example.keepgoing.Class.User;
import com.example.keepgoing.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButtonOpen;
    private ExtendedFloatingActionButton floatingActionButtonAdd, floatingActionButtonRemove;
    private Animation rotateOpen, rotateClose, toBottom, fromBottom;
    private Boolean isOpen = false;
    private User user = null;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Button ButtonAdd, ButtonRemove, ButtonCancel;
    private TextInputLayout TextInputLayoutPlan;
    private ArrayList<Plan> plans;
    private String DAY = "SUNDAY";
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workout,container,false);
        plans = new ArrayList<>();
        floatingActionButtonOpen = view.findViewById(R.id.floatingActionButtonOpen);
        floatingActionButtonAdd = view.findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonRemove = view.findViewById(R.id.floatingActionButtonRemove);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getUser();
        setAddAndRemove();
        return view;
    }
    private void getUser(){
        if(firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    setPlans();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }
    private void setPlans(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(DAY).child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plans.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Plan plan = dataSnapshot.getValue(Plan.class);
                    plans.add(plan);
                }
                WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), plans, user);
                recyclerView.setAdapter(workoutAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private void setAddAndRemove(){
        floatingActionButtonOpen.setOnClickListener( v -> {
            isOpen = !isOpen;
            rotateOpen = AnimationUtils.loadAnimation(view.getContext(),R.anim.rotate_open);
            rotateClose = AnimationUtils.loadAnimation(view.getContext(),R.anim.rotate_close);
            fromBottom = AnimationUtils.loadAnimation(view.getContext(),R.anim.from_bottom);
            toBottom = AnimationUtils.loadAnimation(view.getContext(),R.anim.to_bottom);
            if (isOpen) {
                floatingActionButtonAdd.setVisibility(View.VISIBLE);
                floatingActionButtonRemove.setVisibility(View.VISIBLE);
                floatingActionButtonAdd.setAnimation(fromBottom);
                floatingActionButtonRemove.setAnimation(fromBottom);
                floatingActionButtonOpen.setAnimation(rotateOpen);
                floatingActionButtonAdd.setClickable(true);
                floatingActionButtonRemove.setClickable(true);
                floatingActionButtonAdd.setOnClickListener( v1 -> AddPlanDialog() );
                floatingActionButtonRemove.setOnClickListener( v1 -> RemovePlanDialog() );
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
    private void AddPlanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_plan,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutPlan = dialogView.findViewById(R.id.TextInputLayoutPlan);
        ButtonAdd = dialogView.findViewById(R.id.ButtonAdd);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButtonCancel.setOnClickListener( v -> alertDialog.cancel() );
        ButtonAdd.setOnClickListener( v -> {
            if(TextInputLayoutPlan.getEditText().getText().toString().equals(""))
                TextInputLayoutPlan.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutPlan.setHelperText("");
            if(!(TextInputLayoutPlan.getEditText().getText().toString().equals(""))){
                alertDialog.cancel();
                String currentDateTime = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(DAY).child(user.getUid()).child(currentDateTime);
                databaseReference.setValue(new Plan(TextInputLayoutPlan.getEditText().getText().toString(), currentDateTime, DAY));
                new PopUpMSG(view.getContext(), getResources().getString(R.string.AddPlan), getResources().getString(R.string.PlanSuccessfullyAdded));
            }
        });
    }
    private void RemovePlanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        PlanPick();
        ButtonCancel.setOnClickListener( v -> alertDialog.cancel() );
        ButtonRemove.setOnClickListener(v -> {
            if(TextInputLayoutPlan.getEditText().getText().toString().equals(""))
                TextInputLayoutPlan.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutPlan.setHelperText("");
            if(!TextInputLayoutPlan.getEditText().getText().toString().equals("")) {
                alertDialog.cancel();
                for(int i=0; i<plans.size();i++)
                    if(TextInputLayoutPlan.getEditText().getText().toString().equals(plans.get(i).getPlanName() + " - " + plans.get(i).getDate())) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Plans").child(DAY).child(user.getUid()).child(plans.get(i).getDate());
                        databaseReference.setValue(null);
                        new PopUpMSG(view.getContext(), getResources().getString(R.string.AddPlan), getResources().getString(R.string.PlanSuccessfullyRemoved));
                    }
            }
        });
    }
    private void PlanPick(){
        TextInputLayoutPlan.getEditText().setOnClickListener( v -> {
            String Remove_plans[] = new String[plans.size()];
            for(int i=0; i<plans.size(); i++)
                Remove_plans[i] = plans.get(i).getPlanName() + " - " + plans.get(i).getDate();
            setDialog(Remove_plans,getResources().getString(R.string.RemovePlan), TextInputLayoutPlan.getEditText());
        });
    }
    private void setDialog(String[] array, String title, TextView textViewPick){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, array);
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
    public void setDay(String day) {
        DAY = day;
    }
}