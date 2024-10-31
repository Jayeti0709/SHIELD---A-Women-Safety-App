package com.example.womensateyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Calltoperson extends AppCompatActivity {
    RecyclerView recyclerView1;
    MainAdapter2 mainAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calltoperson);

        recyclerView1 = findViewById(R.id.rv);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Emergency Contact"), MainModel.class)
                .build();

        mainAdapter2 = new MainAdapter2(options);
        recyclerView1.setAdapter(mainAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter2.stopListening();
    }

    public void backbutton(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    txtSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    txtSearch(query);
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Emergency Contact")
                        .orderByChild("Name")
                        .startAt(str)
                        .endAt(str + "\uf8ff"), MainModel.class)
                .build();

        MainAdapter2 newAdapter = new MainAdapter2(options);
        newAdapter.startListening();
        recyclerView1.setAdapter(newAdapter);
    }
}
