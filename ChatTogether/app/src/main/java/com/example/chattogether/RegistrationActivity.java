package com.example.chattogether;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextInputLayout regEmail;
    TextInputLayout regPassword;
    TextInputLayout regName;
    TextInputLayout cPassword;

    TextInputEditText regEmailView;
    TextInputEditText regNameView;
    TextInputEditText regPassView;
    TextInputEditText CPassView;

    CircleImageView profileView;

    String Email;
    String Password;
    String CPassword;
    String Name;
    int requestCode;

    Button registerBtn;


    public void setInitialState()
    {
        regEmail = findViewById(R.id.reg_email);
        regName = findViewById(R.id.reg_name);
        regPassword = findViewById(R.id.reg_password);
        cPassword = findViewById(R.id.cpassword);

        regEmailView = findViewById(R.id.regEmailView);
        regPassView = findViewById(R.id.regPassView);
        regNameView = findViewById(R.id.regNameView);
        CPassView = findViewById(R.id.cpassView);
        profileView = findViewById(R.id.profile_image);

        registerBtn = findViewById(R.id.registerBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setInitialState();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                boolean isValidated = validateForm();
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Title"),);*/
                mGetContent.launch("image/*");
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    profileView.setImageURI(uri);
                }
            });


    private void removeErrors() {
        regEmail.setError(null);
        regPassword.setError(null);
        cPassword.setError(null);
        regName.setError(null);
    }

    private boolean validateForm() {

        Email = regEmailView.getText().toString();
        Password = regPassView.getText().toString();
        CPassword = CPassView.getText().toString();
        Name = regNameView.getText().toString();

        if(Email.isEmpty())
        {
            regEmail.setError("*This field is required.");
            return false;
        }
        if(Email.indexOf('@')==-1)
        {
            regEmail.setError("*Please enter valid Email Address.");
            return false;
        }

        String numbers = "(.*[0-9].*)";
        String lowerCaseChars = "(.*[a-zA-Z ].*)";

        if (Name.isEmpty())
        {
            regName.setError("*This field is Required.");
            return false;
        }
        if(!Name.matches(lowerCaseChars))
        {
            regName.setError("*Enter Valid name.");
            return false;
        }
        if(Password.isEmpty())
        {
            regPassword.setError("*This field is required.");
            return false;
        }
        else if(Password.length() > 16 || Password.length() < 8)
        {
            regPassword.setError("*Password must be of 8 to 16 characters.");
            return false;
        }
        else if (!Password.matches(lowerCaseChars ))
        {
            regPassword.setError("*Password must have at least one alphabet");
            return false;
        }
        else if (!Password.matches(numbers ))
        {
            regPassword.setError("*Password must have at least one number");
            return false;
        }
        if(!Password.equals(CPassword))
        {
            cPassword.setError("*Password And Confirm Password does not match.");
            return false;
        }
        return true;
    }

    public void redirectLogin(View view)
    {
        startActivity(new Intent(getBaseContext(),LoginActivity.class));
        finish();
    }
}