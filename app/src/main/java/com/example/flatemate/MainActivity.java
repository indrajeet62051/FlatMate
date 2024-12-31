package com.example.flatemate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    TextInputEditText editTextemail,editTextpassword;
    Button btn_Login;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView,forgetpassword;


    //for forget password dialog box
    private TextInputEditText edtemail;
    private Button resetbtn;
    private String forgetemail;
    private FirebaseAuth auth;
    private Button cancel;




    @Override
    public void onStart() {
        super.onStart();



        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this,home.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //for status bar of phone like time bettry color
        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(decorView);
        if (windowInsetsController != null) {
            // Set the status bar text color based on the background (light for dark backgrounds, dark for light backgrounds)
            windowInsetsController.setAppearanceLightStatusBars(false); // Use light text color if the background is dark
        }
        // Optional: Set a custom status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.lighblue));



        mAuth=FirebaseAuth.getInstance();
        editTextemail=findViewById(R.id.email);
        editTextpassword=findViewById(R.id.password);
        btn_Login=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.registerNow);
        forgetpassword=findViewById(R.id.fp);















        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(MainActivity.this,Register.class);
                startActivity(i1);

            }
        });


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email,password;
                email=String.valueOf(editTextemail.getText());
                password=String.valueOf(editTextpassword.getText());

                if (TextUtils.isEmpty(email)&TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"fill all details",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(TextUtils.isEmpty(email)|| !isValidEmail(email)){
                    Toast.makeText(MainActivity.this,"enter valid email",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"enter valid password",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "sign in successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,home.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

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