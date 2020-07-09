package com.example.authentiation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class login extends AppCompatActivity {
EditText lemail,lpassword;
Button logbutton;
TextView head,head1,head2,admin;
ProgressBar pbar;
FirebaseAuth fauth;
ImageView nitte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lemail=findViewById(R.id.le1);
        lpassword=findViewById(R.id.lp1);
        logbutton=findViewById(R.id.loginbutton);
        head=findViewById(R.id.lh1);
        head1=findViewById(R.id.lh2);
        head2=findViewById(R.id.lt2);
        pbar=findViewById(R.id.lprogressBar);
        admin=findViewById(R.id.tad);
        nitte = findViewById(R.id.nit2);

        nitte.setImageResource(R.drawable.nittelogo);

        fauth=FirebaseAuth.getInstance();

        logbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loemail = lemail.getText().toString().trim();
                String lopas = lpassword.getText().toString().trim();

                if(TextUtils.isEmpty((loemail))){
                    lemail.setError(("email is required"));
                    return;
                }

                if(TextUtils.isEmpty((lopas))){
                    lemail.setError(("password is rewuired"));
                    return;
                }

                if(lopas.length()<6){
                    lpassword.setError("password must be min of 6");
                    return;
                }

                pbar.setVisibility(View.VISIBLE);

                //authenticate user
                fauth.signInWithEmailAndPassword(loemail,lopas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"user loged in",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(login.this,"error! no user"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        head1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

        head2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetmail =new EditText(v.getContext());
                AlertDialog.Builder passwordresetdailog=new AlertDialog.Builder(v.getContext());
                passwordresetdailog.setTitle("password reset?");
                passwordresetdailog.setMessage("enter your email ro Receive reset link ");
                passwordresetdailog.setView(resetmail);

                passwordresetdailog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail= resetmail.getText().toString().trim();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this,"reset link sent to your email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this,"error!!reset link is  not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordresetdailog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordresetdailog.create().show();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText adminpass =new EditText(v.getContext());
                AlertDialog.Builder password=new AlertDialog.Builder(v.getContext());
                password.setTitle("Hello Admin!!");
                password.setMessage("Please Enter Password ");
                password.setView(adminpass);

                password.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail= adminpass.getText().toString().trim();
                       if( mail.compareTo("123456")==0){

                           startActivity(new Intent(getApplicationContext(),showuploads.class));
                       }
                       else{
                           Toast.makeText(login.this, "password incorrect"+mail, Toast.LENGTH_LONG).show();
                       }
                    }
                });
                password.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                password.create().show();
            }
        });



    }
}
