package com.example.myuber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myuber.models.Driver;
import com.example.myuber.models.Passenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverSignInActivity extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPass;
    private TextInputLayout textInputConfirmPass;

    private Button logInSignUpButton;
    private TextView toggleLogInSignUp;

    private boolean isLoginModeActive;

    FirebaseDatabase database;
    DatabaseReference driversDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_in);

        database = FirebaseDatabase.getInstance();
        driversDatabaseReference = database.getReference().child("drivers");
        mAuth = FirebaseAuth.getInstance();

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputName = findViewById(R.id.textInputName);
        textInputPass = findViewById(R.id.textInputPass);
        textInputConfirmPass = findViewById(R.id.textInputConfirmPass);

        logInSignUpButton = findViewById(R.id.logInSignUpButton);
        toggleLogInSignUp = findViewById(R.id.toggleLogInSignUp);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("?????????????? E-mail");
            return false;
        } else {
            textInputEmail.setError("");
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("?????????????? ??????");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("?????? ?????????????? ??????????????");
            return false;
        } else {
            textInputName.setError("");
            return true;
        }
    }

    private boolean validatePassword() {
        String passInput = textInputPass.getEditText().getText().toString().trim();
        String confirmPassInput = textInputConfirmPass.getEditText().getText().toString().trim();

        if (passInput.isEmpty()) {
            textInputPass.setError("?????????????? ????????????");
            return false;
        } else if (passInput.length() < 6) {
            textInputPass.setError("???????????? ???????????? ???????? ???????????? 5 ????????????????");
            return false;
        } else {
            textInputPass.setError("");
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String passInput = textInputPass.getEditText().getText().toString().trim();
        String confirmPassInput = textInputConfirmPass.getEditText().getText().toString().trim();

        if (!confirmPassInput.equals(passInput)) {
            textInputConfirmPass.setError("???????????? ???? ??????????????????");
            return false;
        } else {
            textInputConfirmPass.setError("");
            return true;
        }
    }

    public void loginSignUpUser(View view) {
        String email = textInputEmail.getEditText().getText().toString().trim();
        String name = textInputName.getEditText().getText().toString().trim();
        String pass = textInputPass.getEditText().getText().toString().trim();

        if (!validateEmail()  | !validateName() | !validatePassword()) {
            return;
        }
        if (isLoginModeActive) {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("User", "???????? ?? ??????????????");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(DriverSignInActivity.this, DriverMapsActivity.class));
                    } else {
                        Log.i("User", "???? ?????????????? ??????????: " + task.getException());
                    }
                }
            });
        } else {
            if (!validateEmail()  | !validateName() | !validatePassword() | !validateConfirmPassword()) {
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("User", "?????????????????????? ???????????? ??????????????");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Driver driver = new Driver();
                        driver.setEmail(user.getEmail());
                        driver.setName(textInputName.getEditText().getText().toString().trim());
                        driver.setDriverID(driversDatabaseReference.push().getKey());
                        driversDatabaseReference.push().setValue(driver);
                        startActivity(new Intent(DriverSignInActivity.this, DriverMapsActivity.class));
                    } else {
                        Log.i("User", "?????????????????????? ???? ????????????: " + task.getException());
                    }
                }
            });
        }
    }

    public void loginSignUpToggle(View view) {
        if (isLoginModeActive) {
            isLoginModeActive = false;
            logInSignUpButton.setText("????????????????????????????????????");
            toggleLogInSignUp.setText("??????????????, ???????? ???? ?????? ????????????????????????????????");
            textInputConfirmPass.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            logInSignUpButton.setText("??????????");
            toggleLogInSignUp.setText("??????????????, ?????????? ????????????????????????????????????");
            textInputConfirmPass.setVisibility(View.GONE);
        }
    }
}