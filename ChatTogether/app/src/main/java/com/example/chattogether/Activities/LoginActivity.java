package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chattogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pd.chocobar.ChocoBar;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;
    TextInputEditText emailView;
    TextInputEditText passView;

    String Email;
    String Password;

    Button loginBtn;

    ProgressDialog dialog;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference tokenReference;

    public void setInitialState()
    {
        emailView = findViewById(R.id.emailView);
        passView = findViewById(R.id.passView);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait..");

        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        tokenReference = database.getReference().child("tokens");

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
                    dialog.show();
                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                                    if (!TextUtils.isEmpty(token)) {
                                        tokenReference.child(auth.getUid()).setValue(token);
                                    }
                                }).addOnFailureListener(e -> {
                                    //handle e
                                }).addOnCanceledListener(() -> {
                                    //handle cancel
                                });
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Sign in successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }
                            else if(!task.isSuccessful())
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException userNotFound)
                                {
                                    dialog.dismiss();
                                    Log.d("SIGNIN", "onComplete: weak_password");
                                    email.setError("*Email does not Exist.");
                                    // TODO: take your actions!
                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                {
                                    dialog.dismiss();
                                    Log.d("SIGNIN", "onComplete: malformed_email");
                                    password.setError("*Wrong Password.");
                                    // TODO: Take your action
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    Log.d("Sign",e.toString());
                                }
                            }
                            else {
                                dialog.dismiss();
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
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
        startActivity(new Intent(getBaseContext(),RegistrationActivity.class));
        finish();
    }

    public void redirectForgotPass(View v)
    {
        EditText emailView = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog  = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter your Registered Email on Chat Together.");
        passwordResetDialog.setView(emailView);

        passwordResetDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = emailView.getText().toString();
                if(email.isEmpty())
                {
                    ChocoBar.builder().setActivity(LoginActivity.this)
                            .setText("Enter email in Dialog box")
                            .setDuration(ChocoBar.LENGTH_LONG)
                            .red()  // in built green ChocoBar
                            .show();
                }
                else {
                    auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(task.isSuccessful())
                            {
                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                Log.d("EMAIL_CHECK",email+isNewUser);
                                if (isNewUser) {
                                    ChocoBar.builder().setActivity(LoginActivity.this)
                                            .setText("Email does not exist")
                                            .setDuration(ChocoBar.LENGTH_LONG)
                                            .red()  // in built green ChocoBar
                                            .show();
                                } else {

                                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful())
                                            {
                                                try
                                                {
                                                    throw task.getException();
                                                }
                                                catch (Exception e) {
                                                    ChocoBar.builder().setActivity(LoginActivity.this)
                                                            .setText("Something went wrong")
                                                            .setDuration(ChocoBar.LENGTH_LONG)
                                                            .red()  // in built green ChocoBar
                                                            .show();
                                                }
                                            }
                                            else {
                                                ChocoBar.builder().setActivity(LoginActivity.this)
                                                        .setText("Password reset Link is sent to your EmailID")
                                                        .setDuration(ChocoBar.LENGTH_LONG)
                                                        .green()  // in built green ChocoBar
                                                        .show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ChocoBar.builder().setActivity(LoginActivity.this)
                                    .setText("Please enter valid Email")
                                    .setDuration(ChocoBar.LENGTH_LONG)
                                    .red()  // in built green ChocoBar
                                    .show();
                        }
                    });
                }
            }
        });

        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}