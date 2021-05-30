package com.example.servicesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText conpassword;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);
        radioGroup = findViewById(R.id.rg);

    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {

        if (!conpassword.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(this, "Check your password", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Finals.user = task.getResult().getUser().getUid();
                                RadioButton r = findViewById(radioGroup.getCheckedRadioButtonId());
                                User user = new User(email.getText().toString(), r.getText().toString());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db
                                        .collection("users")
                                        .document(Finals.user)
                                        .set(user)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(SignupActivity.this, "user created", Toast.LENGTH_SHORT).show();
                                            //navigate to main activity
                                            Intent i = new Intent(SignupActivity.this, TabActivity.class);
                                            startActivity(i);
                                            finish();
                                        });
                            } else {
                                Toast.makeText(SignupActivity.this, "Cannot create user", Toast.LENGTH_SHORT).show();
                                Log.d("trace", "Error: " + task.getException());
                            }
                        }
                    });
        }
    }


}