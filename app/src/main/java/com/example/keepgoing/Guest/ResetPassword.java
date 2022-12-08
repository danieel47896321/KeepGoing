package com.example.keepgoing.Guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepgoing.Class.Loading;
import com.example.keepgoing.Class.PopUpMSG;
import com.example.keepgoing.MainActivity;
import com.example.keepgoing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ResetPassword extends AppCompatActivity {
    private Button ButtonFinish;
    private TextInputLayout TextInputLayoutEmail;
    private ImageView BackIcon;
    private TextView Title;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Loading loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }
    private void init(){
        setID();
        BackIcon();
        EndIcon();
        ResetPassword();
    }
    private void setID(){
        BackIcon = findViewById(R.id.BackIcon);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.ResetPassword);
        ButtonFinish = findViewById(R.id.ButtonFinish);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
    }
    private void EndIcon() {
        TextInputLayoutEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Clear(TextInputLayoutEmail); }
        });
    }
    private void Clear(TextInputLayout input){
        input.setHelperText("");
        input.getEditText().setText("");
    }
    private boolean isEmailValid(CharSequence email) { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    private void ResetPassword(){
        ButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextInputLayoutEmail.getEditText().getText().length() > 0) {
                    if(!isEmailValid(TextInputLayoutEmail.getEditText().getText().toString()))
                        TextInputLayoutEmail.setHelperText(getResources().getString(R.string.InvalidEmail));
                    else {
                        loading = new Loading(ResetPassword.this);
                        firebaseAuth.fetchSignInMethodsForEmail(TextInputLayoutEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                loading.stop();
                                if(task.isSuccessful()) {
                                    if (!task.getResult().getSignInMethods().isEmpty()) {
                                        FirebaseAuth.getInstance().sendPasswordResetEmail(TextInputLayoutEmail.getEditText().getText().toString());
                                        new PopUpMSG(ResetPassword.this, getResources().getString(R.string.ResetPassword), getResources().getString(R.string.ResetLink), MainActivity.class);
                                    } else
                                        TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailNotExist));
                                }
                                else
                                    new PopUpMSG(ResetPassword.this,getResources().getString(R.string.Error),getResources().getString(R.string.ErrorMSG));
                            }
                        });
                    }
                }
                else
                    TextInputLayoutEmail.setHelperText(getResources().getString(R.string.Required));
            }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(MainActivity.class); }
        });
    }
    private void StartActivity(Class Destination){
        startActivity(new Intent(ResetPassword.this, Destination));
        finish();
    }
    @Override
    public void onBackPressed() { StartActivity(MainActivity.class); }
}