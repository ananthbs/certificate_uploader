package com.example.authentiation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    Button uploadbutton;
    TextView name,email,usn;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userid;
    ImageView prof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.mt1);
        email=findViewById(R.id.mt2);
        usn=findViewById(R.id.mt3);
        uploadbutton = findViewById(R.id.upload);
        prof=findViewById(R.id.mi1);

        prof.setImageResource(R.drawable.profilephoto);

        fauth = FirebaseAuth.getInstance();
        fstore =FirebaseFirestore.getInstance();


        userid = fauth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("fname"));
                email.setText(documentSnapshot.getString("email"));
                usn.setText(documentSnapshot.getString("usn"));
            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),upload.class));
            }
        });
        

    }
        public void logout(View view){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),login.class));
            finish();
        }

}
