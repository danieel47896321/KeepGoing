package com.example.keepgoing.Guest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.keepgoing.Class.Loading;
import com.example.keepgoing.Class.PopUpMSG;
import com.example.keepgoing.Class.User;
import com.example.keepgoing.R;
import com.example.keepgoing.User.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends Fragment {
    private View view;
    private TextView ResetPassword;
    private TextInputLayout TextInputLayoutEmail, TextInputLayoutPassword;
    private Button ButtonSignIn;
    private Loading loading;
    private User user = new User();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://keepgoing-c71f6-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        TextInputLayoutEmail = view.findViewById(R.id.TextInputLayoutEmail);
        TextInputLayoutPassword = view.findViewById(R.id.TextInputLayoutPassword);
        ButtonSignIn = view.findViewById(R.id.ButtonSignIn);
        ResetPassword = view.findViewById(R.id.ResetPassword);
        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            loading = new Loading(view.getContext());
            getUser();
        }
        EndIcon();
        SignInCheck();
        ResetPassword();
        return view;
    }
    private void ResetPassword() {
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPassword.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    private void EndIcon() {
        TextInputLayoutEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayoutEmail.setHelperText("");
                TextInputLayoutEmail.getEditText().setText("");
            }
        });
    }
    private boolean isEmailValid(CharSequence email) { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    private void CheckValues(){
        if(TextInputLayoutEmail.getEditText().getText().length()<1)
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.Required));
        else if(!isEmailValid(TextInputLayoutEmail.getEditText().getText().toString()))
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.InvalidEmail));
        else
            TextInputLayoutEmail.setHelperText("");
        if(TextInputLayoutPassword.getEditText().getText().length()<1)
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
        else if(TextInputLayoutPassword.getEditText().getText().length()<6)
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.MinimumChars));
        else
            TextInputLayoutPassword.setHelperText("");
    }
    private void SignInCheck(){
        ButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValues();
                if(TextInputLayoutEmail.getEditText().getText().length() > 0 && TextInputLayoutPassword.getEditText().getText().length() > 5 && isEmailValid(TextInputLayoutEmail.getEditText().getText().toString()) && TextInputLayoutPassword.getEditText().getText().length() > 0)
                    SignIn();
            }
        });
    }
    private void SignIn(){
        firebaseAuth.signInWithEmailAndPassword(TextInputLayoutEmail.getEditText().getText().toString(), TextInputLayoutPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loading = new Loading(view.getContext());
                if (task.isSuccessful()) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                        getUser();
                    else {
                        new PopUpMSG(view.getContext(),getResources().getString(R.string.SignIn),getResources().getString(R.string.CheckEmailVerify));
                        loading.stop();
                    }
                } else
                    CheckEmailExists();
            }
        });
    }
    private void CheckEmailExists(){
        firebaseAuth.fetchSignInMethodsForEmail(TextInputLayoutEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                loading.stop();
                if(task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().isEmpty())
                        TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailNotExist));
                    else {
                        TextInputLayoutEmail.setHelperText("");
                        if (TextInputLayoutPassword.getEditText().getText().length() < 1)
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
                        else if (TextInputLayoutPassword.getEditText().getText().length() < 6)
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.MinimumChars));
                        else
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.WrongPassword));
                    }
                }
                else
                    new PopUpMSG(view.getContext(),getResources().getString(R.string.Error),getResources().getString(R.string.ErrorMSG));
            }
        });
    }
    private void getUser(){
        if(firebaseAuth.getCurrentUser() != null) {
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loading.stop();
                    User user = snapshot.getValue(User.class);
                    Home(user);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }
    private void Home(User user){
        Intent intent = new Intent(getActivity(), Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
        getActivity().finish();
    }
}