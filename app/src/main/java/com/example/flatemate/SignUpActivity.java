package com.example.flatemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signup_name, signup_phone, signup_vehicle, signup_email, signup_password;
    Spinner flat_no;
    Button signup_button;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference referance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        signup_name = findViewById(R.id.signup_name);
        signup_phone = findViewById(R.id.signup_phone);
        signup_vehicle = findViewById(R.id.signup_vehicle);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        flat_no = findViewById(R.id.flat_no);
        signup_button = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        database = FirebaseDatabase.getInstance();
        referance = database.getReference("users");

        // Set up the dropdown menu for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.flat_numbers, // Reference to the string-array in strings.xml
                android.R.layout.simple_spinner_item // Default layout for spinner items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Set dropdown layout
        flat_no.setAdapter(adapter); // Bind adapter to Spinner

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signup_name.getText().toString();
                String phone = signup_phone.getText().toString();
                String flatno = flat_no.getSelectedItem().toString(); // Get selected item from Spinner
                String vehicle = signup_vehicle.getText().toString();
                String email = signup_email.getText().toString();
                String password = signup_password.getText().toString();

                // Create a new user object
                HelperClass helperClass = new HelperClass(name, phone, flatno, vehicle, email, password);

                // Save the user data to Firebase
                referance.child(name).setValue(helperClass);

                Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "You are redirected to Login Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
