package com.example.campusrecruitment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.campusrecruitment.R;
import com.example.campusrecruitment.StudentRegisterActivity;

public class OTPDialog extends AppCompatDialogFragment {
    @NonNull
    EditText otpView;
    Integer OTP;
    String category;
    int otpReceived;
    public static int isVerified;
    public OTPDialog(Integer OTP,String category)
    {
        this.OTP = OTP;
        this.category = category;
        isVerified = -1;
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_otpdialog, null);

        otpView = view.findViewById(R.id.otpfield);

        builder.setView(view).setTitle("Enter OTP")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isVerified=0;
                            dismiss();
                    }
                })
        .setPositiveButton("Submit", submitBtn).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               if(isVerified == -1)
                   isVerified=0;
            }
        });

        return builder.create();
    }

    DialogInterface.OnClickListener submitBtn= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            otpReceived = Integer.parseInt(otpView.getText().toString());

            if(otpReceived == OTP)
            {
                isVerified= 1;
            }
            else {
                isVerified = 0;
            }
        }
    };
}
