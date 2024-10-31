package com.example.womensateyapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.relation.setText(model.getRelation());
        holder.email.setText(model.getEmailid());
        holder.phoneno.setText(model.getPhoneno());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(v.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1400)
                        .create();

                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.txtName);
                EditText email = view.findViewById(R.id.txtEmail);
                EditText relation = view.findViewById(R.id.txtRelation);
                EditText phoneno = view.findViewById(R.id.txtPhoneno);
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                name.setText(model.getName());
                email.setText(model.getEmailid());
                relation.setText(model.getRelation());
                phoneno.setText(model.getPhoneno());

                dialogPlus.show();
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();

                        // Get the item's position in the adapter
                        int itemPosition = holder.getAdapterPosition();

                        // Check if the position is valid
                        if (itemPosition != RecyclerView.NO_POSITION) {
                            map.put("Name", name.getText().toString());
                            map.put("Relation", relation.getText().toString());
                            map.put("Phoneno", String.valueOf(phoneno.getText()));
                            map.put("emailid", email.getText().toString());

                            // Update data in Firebase
                            FirebaseDatabase.getInstance().getReference().child("Emergency Contact")
                                    .child(getRef(itemPosition).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            notifyItemRemoved(itemPosition);
                                            notifyItemRangeChanged(itemPosition, getItemCount());
                                            Toast.makeText(v.getContext(), "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(v.getContext(), "Error while Updating.", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Delete data can't be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int itemPosition = holder.getAdapterPosition();
                        if (itemPosition != RecyclerView.NO_POSITION) {
                            // Delete data from Firebase
                            FirebaseDatabase.getInstance().getReference().child("Emergency Contact")
                                    .child(getRef(itemPosition).getKey()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            notifyItemRemoved(itemPosition);
                                            notifyItemRangeChanged(itemPosition, getItemCount());
                                            Toast.makeText(v.getContext(), "Data Deleted Successfully.", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(v.getContext(), "Error while Deleting.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(v.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView name, relation, email, phoneno;
        Button btnEdit, btnDelete,btnBack;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nametext);
            relation = itemView.findViewById(R.id.relationtext);
            email = itemView.findViewById(R.id.Emailtext);
            phoneno = itemView.findViewById(R.id.Phonetext);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnBack=itemView.findViewById(R.id.btnBack);
        }

    }

}



//package com.example.womensateyapp;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget. TextView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.FirebaseDatabase;
//import com.orhanobut.dialogplus.DialogPlus;
//import com.orhanobut.dialogplus.ViewHolder;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {
//
//    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
//        holder.name.setText(model.getName());
//        holder.relation.setText(model.getRelation());
//        holder.email.setText(model.getEmailid());
//        holder.phoneno.setText(model.getPhoneno());
//
//        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.update_popup))
//                        .setExpanded(true, 1400)
//                        .create();
//
//                View view = dialogPlus.getHolderView();
//                EditText name = view.findViewById(R.id.txtName);
//                EditText email = view.findViewById(R.id.txtEmail);
//                EditText relation = view.findViewById(R.id.txtRelation);
//                EditText phoneno = view.findViewById(R.id.txtPhoneno);
//                Button btnUpdate = view.findViewById(R.id.btnUpdate);
//                name.setText(model.getName());
//                email.setText(model.getEmailid());
//                relation.setText(model.getRelation());
//                phoneno.setText(model.getPhoneno());
//
//                dialogPlus.show();
//                btnUpdate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Map<String, Object> map = new HashMap<>();
//                        int itemPosition = holder.getAdapterPosition();
//                        adapter.notifyItemInserted(itemPosition);
//                        map.put("Name", name.getText().toString());
//                        map.put("Relation", relation.getText().toString());
//                        map.put("Phoneno", String.valueOf(phoneno.getText()));
//                        map.put("emailid", email.getText().toString());
//                        FirebaseDatabase.getInstance().getReference().child("Emergency Contact")
//                                .child(getRef(itemPosition).getKey()).updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(holder.name.getContext(), "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        Toast.makeText(holder.name.getContext(), "Error while Updating.", Toast.LENGTH_SHORT).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//
//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
//                builder.setTitle("Are you Sure?");
//                builder.setMessage("Delete data can't be Undo.");
//
//                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        int itemPosition = holder.getAdapterPosition();
//                        if (itemPosition != RecyclerView.NO_POSITION) {
//                            FirebaseDatabase.getInstance().getReference().child("Emergency Contact")
//                                    .child(getRef(itemPosition).getKey()).removeValue();
//                        }
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.show();
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
//        return new myViewHolder(view);
//    }
//
//    class myViewHolder extends RecyclerView.ViewHolder {
//        TextView name, relation, email, phoneno;
//        Button btnEdit, btnDelete;
//
//        public myViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.nametext);
//            relation = itemView.findViewById(R.id.relationtext);
//            email = itemView.findViewById(R.id.Emailtext);
//            phoneno = itemView.findViewById(R.id.Phonetext);
//
//            btnEdit = itemView.findViewById(R.id.btnEdit);
//            btnDelete = itemView.findViewById(R.id.btnDelete);
//        }
//    }
//}
