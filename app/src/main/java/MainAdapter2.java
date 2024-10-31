package com.example.womensateyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainAdapter2 extends FirebaseRecyclerAdapter<MainModel, MainAdapter2.myViewHolder> {
    public static final int REQUEST_CALL_PHONE = 1;

    public MainAdapter2(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter2.myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.relation.setText(model.getRelation());
        holder.email.setText(model.getEmailid());
        holder.phoneno.setText(model.getPhoneno());

        holder.btnCall.setOnClickListener(v -> {
            // Extract the phone number from the model and create a call intent
            String phoneNumber = model.getPhoneno();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                v.getContext().startActivity(callIntent);
                callIntent.putExtra("returnToMain", true);
            } else {
                // Request the CALL_PHONE permission
                ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            }
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, getItemCount());
//          After the call is completed, return to the MainActivity
//            Intent i=new Intent((Activity)MainAdapter2.this,MainActivity.class);

        });



    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item2, parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView name, relation, email, phoneno;
        Button btnCall;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nametext);
            relation = itemView.findViewById(R.id.relationtext);
            email = itemView.findViewById(R.id.Emailtext);
            phoneno = itemView.findViewById(R.id.Phonetext);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }

}
