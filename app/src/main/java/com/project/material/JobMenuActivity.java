package com.project.material;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class JobMenuActivity extends SherlockActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_job_menu);

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

        SharedPreferences sharedPreferences = getSharedPreferences("calculator", MODE_PRIVATE);
        if (sharedPreferences.getString("pref", "personal").equals("personal"))
            findViewById(R.id.layout_questions).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.text_calculations))
                .setTextSize(CommonUtils.mItemButtonTextSize);
        ((TextView) findViewById(R.id.text_notes))
                .setTextSize(CommonUtils.mItemButtonTextSize);
        ((TextView) findViewById(R.id.text_questions))
                .setTextSize(CommonUtils.mItemButtonTextSize);

        findViewById(R.id.layout_calculations).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JobMenuActivity.this,
                                JobsActivity.class);
                        intent.putExtra("state", "personal");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
        );

        findViewById(R.id.layout_notes).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(JobMenuActivity.this,
                                NoteListActivity.class));
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
        );

        findViewById(R.id.layout_questions).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JobMenuActivity.this,
                                JobsActivity.class);
                        intent.putExtra("state", "business");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
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
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.text_calculations)));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(findViewById(R.id.text_calculations)));
                ShareUtils.shareWithTwitter(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;
        }

        return true;
    }

}
