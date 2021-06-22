package com.example.campusrecruitment.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.campusrecruitment.R;

public class OTPDialog extends AppCompatDialogFragment {
    @NonNull
    EditText otpView;
    public static int otpReceived;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_otpdialog,null);

        otpView = view.findViewById(R.id.otpfield);

        builder.setView(view).setTitle("Enter OTP")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                    }
                })
        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                otpReceived = Integer.parseInt(otpView.getText().toString());
                notify();
            }
        });

        return builder.create();
    }
}
