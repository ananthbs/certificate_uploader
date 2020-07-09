package com.example.authentiation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class showuploads extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mrecyclerview;
    private ImageAdapter madapter;

    private ProgressBar mprogresscircle;

    private DatabaseReference mdatabaseref;

    private  ValueEventListener mdblistener;
    private FirebaseStorage mstorage;
    private List<foruploadpress> muploads;
    FirebaseAuth fauth;
    String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showuploads);

        mrecyclerview = findViewById(R.id.recyler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));



        mprogresscircle = findViewById(R.id.progress_circle);
         muploads = new ArrayList<>();
        madapter = new ImageAdapter(showuploads.this, muploads);
        mrecyclerview.setAdapter(madapter);
        fauth = FirebaseAuth.getInstance();


        madapter.setonitemclicklistener(showuploads.this);
        mdatabaseref = FirebaseDatabase.getInstance().getReference("uploads");

        mstorage=FirebaseStorage.getInstance();


       mdblistener = mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                muploads.clear();
                for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                    foruploadpress upload = postsnapshot.getValue(foruploadpress.class);
                    upload.setkey(postsnapshot.getKey());
                    muploads.add(upload);
                }
               madapter.notifyDataSetChanged();
                mprogresscircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(showuploads.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mprogresscircle.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnDownloadClick(int position) {

        foruploadpress selecteditem = muploads.get(position);
        final String selectedkey = selecteditem.getname();

        StorageReference imageRef = mstorage.getReferenceFromUrl(selecteditem.getimageurl());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadFile(uri,selectedkey);
                Toast.makeText(showuploads.this,"downloaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(showuploads.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

                 
    }

    private void downloadFile(Uri uri, String selectedkey) {
       // File file= new File(Environment.getExternalStorageDirectory(),"certificate");
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(selectedkey)
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                //.setDestinationUri(Uri.fromFile(file));

        downloadManager.enqueue(request);
    }

    @Override
    public void OnDeleteClick(int position) {
        foruploadpress selecteditem = muploads.get(position);
        final String selectedkey = selecteditem.getkey();
        StorageReference imageRef = mstorage.getReferenceFromUrl(selecteditem.getimageurl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mdatabaseref.child(selectedkey).removeValue();
                Toast.makeText(showuploads.this, "deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(showuploads.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseref.removeEventListener(mdblistener);

    }
}
