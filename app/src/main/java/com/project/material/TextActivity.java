package com.project.material;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.material.util.CommonUtils;
import com.project.material.util.ShareUtils;
import com.project.material.util.image.ScreenCapture;
import com.project.material.util.menu.MenuUtils;
import com.project.material.util.rate.AppRater;

public class TextActivity extends SherlockActivity {

    private String[] textFiles;
    private String strPos;

    public TextActivity() {
        String[] arrayOfString = new String[5];
        arrayOfString[0] = "manual.html";
        arrayOfString[1] = "tools.html";
        arrayOfString[2] = "blank.html";
        arrayOfString[3] = "safety.html";
        arrayOfString[4] = "guide.html";
        this.textFiles = arrayOfString;
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_text_page);

        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.ad);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        //com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().addTestDevice(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR).build();
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


        ((TextView) findViewById(R.id.textTitle)).setText(getIntent()
                .getStringExtra("title"));
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setDefaultFontSize((int) CommonUtils.mWebViewTextSize);

        strPos = getIntent().getStringExtra("pos");
        webView.loadUrl("file:///android_asset/"
                + this.textFiles[Integer.parseInt(strPos)]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        strPos = savedInstanceState.getString("pos");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pos", strPos);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        MenuUtils.addMenu(menu);

        menu.findItem(MenuUtils.MENU_SAVE).setVisible(false);
        menu.findItem(MenuUtils.MENU_REFRESH_APP).setVisible(false);
        menu.findItem(MenuUtils.MENU_SEARCH).setVisible(false);
        */

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MenuUtils.MENU_HOME:
                onBackPressed();
                break;

            case MenuUtils.MENU_GOTO_PRO:
                AppRater.gotoProVersion(this);
                break;

            case MenuUtils.MENU_RATE_US:
                AppRater.doRate(this);
                break;

            case MenuUtils.MENU_EXIT_APP:
                finish();
                break;

            case MenuUtils.MENU_FACEBOOK_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.webView1)));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.webView1)));
                ShareUtils.shareWithTwitter(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;
        }

        return true;
    }

}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.TextActivity JD-Core Version: 0.6.0
 */