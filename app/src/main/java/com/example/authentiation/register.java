package com.example.authentiation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText uemail,usn,password,cpassword,rname;
    Button register;
    TextView heading,notr;
    ProgressBar progressbar;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userid;
    ImageView nitte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        uemail=findViewById(R.id.le1);
        usn=findViewById(R.id.e2);
        rname=findViewById(R.id.name);
        password=findViewById(R.id.lp1);
        cpassword=findViewById(R.id.p2);
        register=findViewById(R.id.b1);
        heading=findViewById(R.id.lh1);
        notr=findViewById(R.id.lh2);
        progressbar=findViewById(R.id.progressBar);
        nitte = findViewById(R.id.nit);

        nitte.setImageResource(R.drawable.nittelogo);

        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        if(fauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uemail.getText().toString().trim();
                String pas = password.getText().toString().trim();
                String cpas = cpassword.getText().toString().trim();
                final String fullName=rname.getText().toString().trim();
                final String susn = usn.getText().toString().trim();
                final String semail = uemail.getText().toString().trim();


                if(TextUtils.isEmpty((email))){
                    uemail.setError(("email is required"));
                    return;
                }

                if(TextUtils.isEmpty((pas))){
                    uemail.setError(("password is required"));
                    return;
                }

                if(pas.compareTo(cpas)!=0){
                    cpassword.setError("password should be same!!");
                    return;
                }

                if(pas.length()<6){
                    password.setError("password must be min of 6");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                fauth.createUserWithEmailAndPassword(email,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //send verification email
                           /* FirebaseUser user = fauth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(register.this,"Verificatiom mail sent",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   Log.d("TAG","onFailure: Email not sent"+e.getMessage());
                                }
                            });

                            */
                            Toast.makeText(register.this,"user created",Toast.LENGTH_SHORT).show();
                            userid = fauth.getCurrentUser().getUid();
                            DocumentReference documentreference = fstore.collection("users").document(userid);
                            Map<String,Object> user= new HashMap<>();
                            user.put("fname",fullName);
                            user.put("email",semail);
                            user.put("usn",susn);
                           documentreference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSuccess:User Profile is created for "+userid);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d(TAG,"onFailure:User Profile is not created  "+e.getMessage());
                               }
                           });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(register.this,"error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        notr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}
