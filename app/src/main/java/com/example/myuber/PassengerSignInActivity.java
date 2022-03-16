package com.example.myuber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class PassengerSignInActivity extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPass;
    private TextInputLayout textInputConfirmPass;

    private Button logInSignUpButton;
    private TextView toggleLogInSignUp;

    private boolean isLoginModeActive;

    FirebaseDatabase database;
    DatabaseReference passengersDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_sign_in);

        database = FirebaseDatabase.getInstance();
        passengersDatabaseReference = database.getReference().child("passengers");
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
            textInputEmail.setError("Введите E-mail");
            return false;
        } else {
            textInputEmail.setError("");
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Введите имя");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("Имя слишком длинное");
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
            textInputPass.setError("Введите пароль");
            return false;
        } else if (passInput.length() < 6) {
            textInputPass.setError("Пароль должен быть больше 5 символов");
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
            textInputConfirmPass.setError("Пароли не совпадают");
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
                        Log.i("User", "Вход в систему");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Log.i("User", "Не удалось войти: " + task.getException());
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
                        Log.i("User", "Регистрация прошла успешно");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Passenger passenger = new Passenger();
                        passenger.setEmail(user.getEmail());
                        passenger.setName(textInputName.getEditText().getText().toString().trim());
                        passenger.setPassengerID(passengersDatabaseReference.push().getKey());
                        passengersDatabaseReference.push().setValue(passenger);
                    } else {
                        Log.i("User", "Регистрация не прошла: " + task.getException());
                    }
                }
            });
        }
    }

    public void loginSignUpToggle(View view) {
        if (isLoginModeActive) {
            isLoginModeActive = false;
            logInSignUpButton.setText("Зарегистрироваться");
            toggleLogInSignUp.setText("Нажмите, если вы уже зарегестрированы");
            textInputConfirmPass.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            logInSignUpButton.setText("Войти");
            toggleLogInSignUp.setText("Нажмите, чтобы зарегестрироваться");
            textInputConfirmPass.setVisibility(View.GONE);
        }
    }
}