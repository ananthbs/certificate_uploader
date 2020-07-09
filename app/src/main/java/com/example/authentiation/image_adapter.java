package com.example.authentiation;

import android.content.Context;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;




class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageviewholder> {
        private Context mcontext;
        private List<foruploadpress> muploads;
        private OnItemClickListener mlistener;



        public ImageAdapter(Context context, List<foruploadpress> uploads){
            mcontext = context;
            muploads = uploads;

        }


    @Override
    public imageviewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.image_item,parent,false);
        return new imageviewholder(v);
    }

    @Override
    public void onBindViewHolder( imageviewholder holder, int position) {

       /* FirebaseAuth fauth = FirebaseAuth.getInstance();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        userid = fauth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
             //   name.setText(documentSnapshot.getString("fname"));
              //  email.setText(documentSnapshot.getString("email"));
               // usn.setText(documentSnapshot.getString("usn"));
            }
        });

        */
        foruploadpress uploadcurrent = muploads.get(position);
        holder.textviewname.setText(uploadcurrent.getname());
        Picasso.with(mcontext)
                .load(uploadcurrent.getimageurl ())
                .placeholder(R.drawable.certi)
                .fit()
                .centerCrop()
                 .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

            return  muploads == null ? 0 : muploads.size();

    }


    public class imageviewholder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            public TextView textviewname;
            public ImageView imageView;
        public imageviewholder(@NonNull View itemView) {
            super(itemView);

            textviewname = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_View_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mlistener != null){
                int position = getAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    mlistener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select Action");
            MenuItem dowhatever = menu.add(Menu.NONE,1,1,"Download");
            MenuItem delete = menu.add(Menu.NONE,2,2,"delete");

            dowhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mlistener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mlistener.OnDownloadClick(position);
                            return true;
                        case 2:
                            mlistener.OnDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
            void OnItemClick(int position);

            void OnDownloadClick(int position);

            void OnDeleteClick(int position);
    }
    public void setonitemclicklistener(OnItemClickListener listener){
        mlistener = listener;
    }

}


