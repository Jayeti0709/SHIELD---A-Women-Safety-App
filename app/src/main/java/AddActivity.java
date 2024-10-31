package com.example.womensateyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
EditText name,relation,email,phoneno;
Button btnadd,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name=(EditText) findViewById(R.id.txtName);
        relation=(EditText) findViewById(R.id.txtRelation);
        phoneno=(EditText) findViewById(R.id.txtPhoneno);
email=(EditText) findViewById(R.id.txtEmail);
btnadd=(Button) findViewById(R.id.btnAdd);
btnBack=(Button) findViewById(R.id.btnBack);

btnadd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
insertData();
clearAll();
    }
});
btnBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(AddActivity.this,AddUser.class);
        startActivity(intent);

        finish();
    }
});
    }
private void insertData(){
        Map<String,Object> map=new HashMap<>();
        map.put("Name",name.getText().toString());
        map.put("Relation",relation.getText().toString());
        map.put("emailid",email.getText().toString());
        map.put("Phoneno",String.valueOf(phoneno.getText()));

    FirebaseDatabase.getInstance().getReference().child("Emergency Contact").push()
            .setValue(map)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AddActivity.this,"Data Inserted Successfully",Toast.LENGTH_SHORT).show();
                }
            })
    .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure( Exception e) {
            Toast.makeText(AddActivity.this,"Error while insertion",Toast.LENGTH_SHORT).show();
        }
    });
}
 private  void clearAll(){
     name.setText("");
     relation.setText("");
     email.setText("");
     phoneno.setText("");
 }



}