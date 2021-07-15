package com.example.chattogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;
    TextInputEditText emailView;
    TextInputEditText passView;

    String Email;
    String Password;

    Button loginBtn;

    private FirebaseAuth auth;

    public void setInitialState()
    {
        emailView = findViewById(R.id.emailView);
        passView = findViewById(R.id.passView);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loginBtn = findViewById(R.id.loginBtn);
    }

    public boolean validateForm()
    {
        Email = emailView.getText().toString();
        Password = passView.getText().toString();

        if(Email.isEmpty())
        {
            email.setError("*This field is required.");
            return false;
        }
        if(Email.indexOf('@')==-1)
        {
            email.setError("*Please enter valid Email Address.");
            return false;
        }

        String numbers = "(.*[0-9].*)";
        String lowerCaseChars = "(.*[a-zA-Z].*)";
        if(Password.isEmpty())
        {
            password.setError("*This field is required.");
            return false;
        }
        else if(Password.length() > 16 || Password.length() < 8)
        {
            password.setError("*Password must be of 8 to 16 characters.");
            return false;
        }
        else if (!Password.matches(lowerCaseChars ))
        {
            password.setError("*Password must have at least one alphabet");
            return false;
        }
        else if (!Password.matches(numbers ))
        {
            password.setError("*Password must have at least one number");
            return false;
        }
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setInitialState();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                boolean isValidated = validateForm();
                if(isValidated)
                {
                    //
                    //Toast.makeText(getBaseContext(),"Verified",Toast.LENGTH_SHORT).show();
                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this,"Sign in successfull",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Something went Wrong, please check network connection and Try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void removeErrors() {
        email.setError(null);
        password.setError(null);
    }

    public void redirectRegistration(View view)
    {
        startActivity(new Intent(getBaseContext(),RegistrationActivity.class));
        finish();
    }
}