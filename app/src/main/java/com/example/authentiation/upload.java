package com.example.authentiation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class upload extends AppCompatActivity {
private static final int PICK_IMAGE_REQUEST = 1;
    Button choose,upload;
    EditText fname;
    TextView show_uploads;
    ProgressBar progressBar;
    ImageView mimageview;
    FirebaseAuth fauth;
    Uri mimageuri;
    String userid;

    private StorageReference mstorageref;
    private DatabaseReference mdatabaseref;
    private StorageTask muploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        choose=findViewById(R.id.chbut);
        fname=findViewById(R.id.file_name);
        upload=findViewById(R.id.upbut);
        show_uploads=findViewById(R.id.shup);
        progressBar=findViewById(R.id.pbar);
        mimageview=findViewById(R.id.iv);

        mstorageref = FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseref = FirebaseDatabase.getInstance().getReference("uploads");
        fauth = FirebaseAuth.getInstance();
        userid = fauth.getCurrentUser().getUid();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openfilechooser();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muploadtask !=null && muploadtask.isInProgress()) {
                    Toast.makeText(upload.this, "upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadfile();
                }
            }
        });



        show_uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimageactivity();

            }
        });
    }
    private  void openfilechooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
              && data != null && data.getData() != null) {
            mimageuri =data.getData();


            Picasso.with(this).load(mimageuri).into(mimageview);//check here with or get
        }
    }
//to get file extensions
    private String getfileextension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    private  void uploadfile(){
        if(mimageuri != null){
            StorageReference filereference = mstorageref.child(//here we can create new folder of userid then add photos to it System.currentTimeMillis()
                    System.currentTimeMillis()+"."+getfileextension(mimageuri));
            muploadtask = filereference.putFile(mimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(upload.this, "Upload sucessfull", Toast.LENGTH_LONG).show();
                    foruploadpress upload = new foruploadpress(fname.getText().toString().trim(),
                            taskSnapshot.getStorage().toString());// check here if error in uploading
                        String uploadId = mdatabaseref.push().getKey();
                        mdatabaseref.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(upload.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
        else{
            Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openimageactivity(){
         Intent intent =new Intent(this,showuploads.class);
            startActivity(intent);

    }

}
