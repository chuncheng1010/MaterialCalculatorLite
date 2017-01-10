package com.project.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.material.util.CommonUtils;
import com.project.material.util.ShareUtils;
import com.project.material.util.image.ScreenCapture;
import com.project.material.util.menu.MenuUtils;
import com.project.material.util.rate.AppRater;

public class HomeActivity extends SherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_home);


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


        /*
        final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();
        //AppRater.appLaunched(this);
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        adRequest.addTestDevice(deviceid);//("9282644A22BF1860D89C67160997E5F8");
        */

        ((TextView) findViewById(R.id.text_folders)).setTextSize(CommonUtils.mItemButtonTextSize);
        ((TextView) findViewById(R.id.text_guides)).setTextSize(CommonUtils.mItemButtonTextSize);
        ((TextView) findViewById(R.id.text_start)).setTextSize(CommonUtils.mItemButtonTextSize);

        findViewById(R.id.layout_folder).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,JobMenuActivity.class));
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
        );

        findViewById(R.id.layout_guide).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,TextActivity.class);
                        intent.putExtra("pos", "0");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
        );

        findViewById(R.id.layout_start).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,CalculatorBasic.class));
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }

                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

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
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.text_folders)));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.text_folders)));
                ShareUtils.shareWithTwitter(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;
        }

        return true;
    }

}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.HomeActivity JD-Core Version: 0.6.0
 */