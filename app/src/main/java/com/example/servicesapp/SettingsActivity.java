package com.example.servicesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void changePass(View view) {
        View v = getLayoutInflater().inflate(R.layout.changepasworddialog, null);
        TextView suggestpass = v.findViewById(R.id.suggest);
        EditText password = v.findViewById(R.id.newpassword);
        suggestpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generated = "";
                Random random = new Random();
                for (int i = 1; i <= 6; ++i) {
                    generated += String.valueOf(random.nextInt(10));
                }
                password.setText(generated);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(v)
                .setPositiveButton("Change", (dialog, which) -> {
                    String newPassword = password.getText().toString();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                    Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Dismiss", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent =
                new Intent(SettingsActivity.this
                        , SignupActivity.class);
        startActivity(intent);
        finishAffinity(); //finishing all activities
    }


}