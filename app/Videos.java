package com.example.womensateyapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

public class Videos extends AppCompatActivity {
Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        button3=findViewById(R.id.button3);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        WebView webView1 = findViewById(R.id.webView1);
        WebView webView2 = findViewById(R.id.webView2);

        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebChromeClient(new WebChromeClient());

        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.setWebChromeClient(new WebChromeClient());

        String video1 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/T7aNSRoDCmg?si=5m6uDN9qgEEJ9Kuo\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        String video2 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/KVpxP3ZZtAc?si=h41hEjPwoExBHIEj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";

        webView1.loadData(video1, "text/html", "utf-8");
        webView2.loadData(video2, "text/html", "utf-8");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void back1(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}



