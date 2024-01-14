package com.shutter.noteflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email_edit_text,password_edit_text,confirm_password_edit_text;
    Button create_account_btn;
    TextView login_btn;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);

        create_account_btn = findViewById(R.id.create_account_btn);
        login_btn = findViewById(R.id.login_btn);

        progressBar = findViewById(R.id.progress_bar);


        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String email = email_edit_text.getText().toString();
               String password = password_edit_text.getText().toString();
               String confirm_password = confirm_password_edit_text.getText().toString();

               boolean isValidated = validateData(email,password,confirm_password);
               if(!isValidated){
                   return;
               }

               createAccount(email,password);



            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class));
            }
        });
    }

    private void createAccount(String email, String password) {
        changeInProgress(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully , check email for verification", Toast.LENGTH_SHORT).show();
                    auth.getCurrentUser().sendEmailVerification();
                    auth.signOut();
                    finish();
                }
                else{
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean validateData(String email,String password,String confirm_password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edit_text.setError("Email Invalid");
            return false;
        }

        if(password.length()<6){
            password_edit_text.setError("Password Length Is Invalid");
            return false;
        }

        if(!confirm_password.equals(password)){
            confirm_password_edit_text.setError("Password doesn't match");
            return false;
        }

        return true;


    }

    private void changeInProgress(boolean inProgress){
        if(inProgress){
            create_account_btn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            create_account_btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}