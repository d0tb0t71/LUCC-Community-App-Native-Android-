package com.shieldsoft.lucc_community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowseURL extends AppCompatActivity {

    WebView web_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_url);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        web_view=findViewById(R.id.web_view_id);

        String link= getIntent().getExtras().getString("url");
        String cp="https://blog.codingblocks.com/2019/start-with-competitive-programming/";
        String dev="https://hackr.io/blog/how-to-learn-programming";

        if(link.equals(cp))
        {
            actionBar.setTitle("Guideline for CP");
        }
        else if(link.equals(dev))
        {
            actionBar.setTitle("Guideline for Development");
        }

        WebSettings webSettings=web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

        web_view.setWebViewClient(new WebViewClient());
        web_view.loadUrl(link);


    }

    @Override
    public void onBackPressed() {

         if(web_view.canGoBack()){
             web_view.goBack();
         }else
         {
             super.onBackPressed();
         }
    }
}