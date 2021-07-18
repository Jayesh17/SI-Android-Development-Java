package com.example.chattogether;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chattogether.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URISyntaxException;

import static androidx.core.content.ContextCompat.startActivity;

public class FireBaseManagement {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageUri;

    private static FireBaseManagement fireBaseManagement=null;

    private FireBaseManagement()
    {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
    }

    public static FireBaseManagement getInstance()
    {
        if(fireBaseManagement==null)
        {
            fireBaseManagement = new FireBaseManagement();
        }
        return fireBaseManagement;
    }

    public void createUser(String email,String pass,String name,String profileUri,String status)
    {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                 DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                 StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                 if(!profileUri.equals("None"))
                 {

                     Uri uri = Uri.parse(profileUri);
                     storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                             if(task.isSuccessful())
                             {
                                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {
                                         imageUri = uri.toString();
                                         User user = new User(name,email,profileUri,status,auth.getUid());
                                         reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.DONE;
                                                 }else {
                                                     Toast.makeText(UserAuthActivity.context,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                                     UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.ERROR;
                                                 }
                                             }
                                         });
                                     }
                                 });
                             }
                             else {
                                 Toast.makeText(UserAuthActivity.context,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                 UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.ERROR;
                             }
                         }
                     });
                 }
                 else {
                     imageUri = "https://firebasestorage.googleapis.com/v0/b/chattogether-19397.appspot.com/o/profile.png?alt=media&token=55170d34-4f1f-4161-9bcb-6b0d4f9e7ad5";
                     User user = new User(name,email,imageUri,status,auth.getUid());
                     reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.DONE;
                             }
                             else {
                                 Toast.makeText(UserAuthActivity.context,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                 UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.ERROR;
                             }
                         }
                     });
                 }
             }
             else {
                 Toast.makeText(UserAuthActivity.context,"Something Went Wrong",Toast.LENGTH_LONG).show();
                 UserAuthActivity.userStatus = ConditionCheckers.REGISTER_USER.ERROR;
             }
            }
        });
    }
}
