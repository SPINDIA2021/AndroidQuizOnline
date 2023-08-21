package com.wrteam.quiz.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;



import com.wrteam.quiz.R;


public class PdfRenderActivity extends AppCompatActivity  {


    private ProgressDialog mProgressDialog;
    WebView web_view;
    private String pdfUrl;
    RelativeLayout relayRefresh;
    ImageView imgBack;
    private static final String KEY_PDF = "key_pdf";




    public static Intent newIntent(Context context, String pdfUrl) {
        Intent newIntent = new Intent(context, PdfRenderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PDF, pdfUrl);
        newIntent.putExtras(bundle);
        return newIntent;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_render);
        web_view = findViewById(R.id.pdfView);
        web_view.invalidate();

        relayRefresh=findViewById(R.id.relayRefresh);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMax(100);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(KEY_PDF))
            this.pdfUrl = getIntent().getExtras().getString(KEY_PDF);

        WebSettings webSettings = web_view.getSettings();

        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        String url = "http://docs.google.com/gview?embedded=true&url="+pdfUrl;
        web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                System.out.println("progess"+progress);
                webSettings.getAllowFileAccess();
                if (progress < 100) {
                    mProgressDialog.show();
                }
                if (progress == 100) {
                    mProgressDialog.dismiss();
                 //   web_view.loadUrl(url);
                }
            }

        });


        relayRefresh.setOnClickListener(view -> {
            web_view.loadUrl(url);
            web_view.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url1) {
                    view.loadUrl(url1);
                    return true;
                }
            });
            web_view.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    System.out.println("progess"+progress);
                    webSettings.getAllowFileAccess();
                    if (progress < 100) {
                        mProgressDialog.show();
                    }
                    if (progress == 100) {
                        mProgressDialog.dismiss();
                        //   web_view.loadUrl(url);
                    }
                }
            });
        });



    }





















}