package com.example.flatemate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference reference;

    private TextView registerNowTextView;
    private Button registerButton;
    private ProgressBar progressBar;
    private EditText usernameEditText;
    private EditText mobileNumberEditText;
    private EditText vehicleNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //for status bar
        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(decorView);
        if (windowInsetsController != null) {
            // Set the status bar text color based on the background (light for dark backgrounds, dark for light backgrounds)
            windowInsetsController.setAppearanceLightStatusBars(false); // Use light text color if the background is dark
        }
        // Optional: Set a custom status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.lighblue));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");

        registerNowTextView = findViewById(R.id.registerNow);
        registerButton = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        usernameEditText = findViewById(R.id.uname);
        mobileNumberEditText = findViewById(R.id.umno);
        vehicleNumberEditText = findViewById(R.id.uvnumber);
        emailEditText = findViewById(R.id.uemail);
        passwordEditText = findViewById(R.id.upassword);
        Spinner spinner = findViewById(R.id.ufno);

        registerNowTextView.setOnClickListener(view -> finish());

        List<String> flatNumbers = new ArrayList<>();
        flatNumbers.add("Flat Number");
        for (int floor = 1; floor <= 5; floor++) {
            for (int flat = 1; flat <= 4; flat++) {
                flatNumbers.add("A-" + floor + "0" + flat);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, flatNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String mobileNumber = mobileNumberEditText.getText().toString().trim();
            String vehicleNumber = vehicleNumberEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String flatNumber = spinner.getSelectedItem().toString();

            // Input validations
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mobileNumber) || TextUtils.isEmpty(vehicleNumber) ||
                    TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || flatNumber.equals("Flat Number")) {
                Toast.makeText(Register.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mobileNumber.length() != 10) {
                Toast.makeText(Register.this, "Valid mobile number is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(Register.this, "Valid email address is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(Register.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Registration successful, store user and vehicle data
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                registerUser(username, mobileNumber, vehicleNumber, email, password, flatNumber, currentUser);
                            }
                        } else {
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void registerUser(String username, String mobileNumber, String vehicleNumber, String email, String password, String flatNumber, FirebaseUser currentUser) {
        // Create a HashMap for the user data
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("mobileNumber", mobileNumber);
        userMap.put("vehicleNumber", vehicleNumber);
        userMap.put("email", email);
        userMap.put("flatNumber", flatNumber);

        // Store the user data in Firebase Realtime Database
        reference.child(currentUser.getUid())
                .setValue(userMap)
                .addOnCompleteListener(saveTask -> {
                    if (saveTask.isSuccessful()) {
                        storeVehicleData(vehicleNumber, username, mobileNumber);
                    } else {
                        Toast.makeText(Register.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeVehicleData(String vehicleNumber, String owner, String mobileNumber) {
        HashMap<String, Object> vehicleMap = new HashMap<>();
        vehicleMap.put("owner", owner);
        vehicleMap.put("mobileNumber", mobileNumber);

        // Store the vehicle data in Firebase Realtime Database
        db.getReference("Vehicles").child(vehicleNumber)
                .setValue(vehicleMap)
                .addOnCompleteListener(vehicleSaveTask -> {
                    if (vehicleSaveTask.isSuccessful()) {
                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Failed to save vehicle data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
