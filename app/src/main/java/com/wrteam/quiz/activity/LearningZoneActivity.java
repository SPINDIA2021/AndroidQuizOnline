package com.wrteam.quiz.activity;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.wrteam.quiz.Constant;
import com.wrteam.quiz.R;
import com.wrteam.quiz.helper.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import kotlin.jvm.internal.Intrinsics;

public class LearningZoneActivity extends YouTubeBaseActivity {

    String api_key = "";
    public ProgressBar prgLoading;
    public WebView mWebView;
    public String id,video_id;
    public Toolbar toolbar;
    TextView tvStartGame;
    FloatingActionButton btnPDFViewer;
    YouTubePlayerView ytPlayer;
    ImageView imgBack;
    TextView txtTitle;

    private final String[] requiredPermissionList = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private final int PERMISSION_CODE = 4040;
    Activity activity;

    private String pdfUrl = "";


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_learningzone);

        activity= new LearningZoneActivity();
        id = getIntent().getStringExtra("id");
        video_id=getIntent().getStringExtra("videoid");
        api_key=getIntent().getStringExtra("apiKey");
        pdfUrl=getIntent().getStringExtra("pdfile");
        prgLoading = findViewById(R.id.prgLoading);
        tvStartGame = findViewById(R.id.tvStartGame);
        tvStartGame.setVisibility(View.VISIBLE);
        mWebView = findViewById(R.id.webView1);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        ytPlayer = (YouTubePlayerView) findViewById(R.id.ytPlayer);
        txtTitle=findViewById(R.id.txtTitle);
        txtTitle.setText(Constant.cate_name);
        btnPDFViewer=findViewById(R.id.btnPDFViewer);



        System.out.println("valueofPDF"+pdfUrl);
        if(pdfUrl.equals(Constant.DOMAIN_URL+"pdf_files/")){
            btnPDFViewer.setVisibility(View.GONE);
        }else {
            btnPDFViewer.setVisibility(View.VISIBLE);
        }
        btnPDFViewer.setOnClickListener(view -> startActivity(PdfRenderActivity.newIntent(LearningZoneActivity.this, pdfUrl)));

        if(video_id.equals("")){
            ytPlayer.setVisibility(View.GONE);
        }else {
            ytPlayer.setVisibility(View.VISIBLE);
        }
        ytPlayer.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    // Implement two methods by clicking on red
                    // error bulb inside onInitializationSuccess
                    // method add the video link or the playlist
                    // link that you want to play In here we
                    // also handle the play and pause
                    // functionality
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(video_id);
                        youTubePlayer.play();
                    }
                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult) {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }

                });
        try {

            mWebView.setClickable(true);
            mWebView.setFocusableInTouchMode(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            // getSupportActionBar().setTitle(Constant.cate_name);
            /*GetPrivacyAndTerms();*/
            if (Utils.isNetworkAvailable(this)) {
                if (!prgLoading.isShown()) {
                    prgLoading.setVisibility(View.VISIBLE);
                }
                mWebView.setVerticalScrollBarEnabled(true);
                mWebView.loadDataWithBaseURL("", getIntent().getStringExtra("message"), "text/html", "UTF-8", "");
                mWebView.setBackgroundColor(getResources().getColor(R.color.bg_color));
            } else {
                //setSnackBar();
            }
            prgLoading.setVisibility(View.GONE);
            tvStartGame.setOnClickListener(v -> {

                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("fromQue", "learning");
                intent.putExtra("learning_id", id);
                startActivity(intent);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final boolean checkAndRequestPermission() {
        ArrayList permissionsNeeded = new ArrayList();
        String[] var4 = this.requiredPermissionList;
        int var5 = var4.length;

        for(int var3 = 0; var3 < var5; ++var3) {
            String permission = var4[var3];
            if (ContextCompat.checkSelfPermission((Context)this, permission) != 0) {
                permissionsNeeded.add(permission);
            }
        }

        Collection $this$toTypedArray$iv = (Collection)permissionsNeeded;
        if (!$this$toTypedArray$iv.isEmpty()) {
            Activity var10000 = (Activity)this;
            $this$toTypedArray$iv = (Collection)permissionsNeeded;
            //int $i$f$toTypedArray = false;
            ActivityCompat.requestPermissions(var10000, (String[])$this$toTypedArray$iv.toArray(new String[0]), this.PERMISSION_CODE);
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.PERMISSION_CODE && grantResults.length != 0) {
            boolean readPermission = grantResults[0] == 0;
            boolean writePermission = grantResults[1] == 0;
            if (readPermission && writePermission) {
                //  this.launchPdf();
            } else {
                Toast.makeText((Context)this, (CharSequence)" Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }





    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }
}