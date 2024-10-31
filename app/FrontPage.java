package com.example.womensateyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.core.view.View;

public class FrontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        // Create a Handler to delay switching to the next page
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Define the next activity you want to start
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }, 1000); // 2000 milliseconds = 2 seconds
    }
}

//package com.example.womensateyapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Looper;
//import android.os.Handler;
//
//
//public class FrontPage extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_front_page);
//
//        // Set an OnClickListener on the root layout
//        View rootLayout = findViewById(R.id.frontpage); // Replace with the actual ID
//        rootLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Start the MainActivity when the layout is clicked
//                Intent intent = new Intent(FrontPage.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            // Define the next activity you want to start
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//            finish();
//        }, 2000); // 2000 milliseconds = 2 seconds
//    }
//}


