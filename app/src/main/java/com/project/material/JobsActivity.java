package com.project.material;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.material.helper.DatabaseHelper;
import com.project.material.util.ShareUtils;
import com.project.material.util.image.ScreenCapture;
import com.project.material.util.menu.MenuUtils;
import com.project.material.util.rate.AppRater;

public class JobsActivity extends SherlockActivity {

    static final String KEY_ID = "ID";
    static final String KEY_TITLE = "title";
    static final String KEY_METHOD = "method";
    static final String KEY_VALUE = "value";
    static final String KEY_OPTION = "option";
    static final String KEY_SHAPE = "shape";

    private String state;
    private JobsListAdapter adapter;
    private ArrayList<HashMap<String, String>> dataList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_folders);

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

        listView = (ListView) findViewById(R.id.listView1);
        // listView.setItemsCanFocus(false);
        listView.setFocusable(false);
        listView.setClickable(false);
        state = getIntent().getStringExtra("state");
        if (TextUtils.isEmpty(state))
            state = "personal";

        getData();

        adapter = new JobsListAdapter(this, dataList, state);
        listView.setAdapter(adapter);

        if (dataList.size() == 0) {
            new AlertDialog.Builder(JobsActivity.this)
                    .setTitle("Folders")
                    .setMessage("There are \"No\" jobs saved at the moment.")
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    onBackPressed();
                                }
                            }
                    ).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, JobMenuActivity.class));
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    public void getData() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        dataList = new ArrayList<HashMap<String, String>>();
        try {
            database = new DatabaseHelper(this, getString(R.string.app_name),
                    null, 1).getWritableDatabase();
            cursor = database
                    .query("jobstable", new String[]{"id", "title", "method",
                            "option", "value", "shape"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(KEY_ID, cursor.getString(cursor
                            .getColumnIndexOrThrow("id")));
                    map.put(KEY_TITLE, cursor.getString(cursor
                            .getColumnIndexOrThrow("title")));
                    map.put(KEY_METHOD, cursor.getString(cursor
                            .getColumnIndexOrThrow("method")));
                    map.put(KEY_VALUE, cursor.getString(cursor
                            .getColumnIndexOrThrow("value")));
                    map.put(KEY_OPTION, cursor.getString(cursor
                            .getColumnIndexOrThrow("option")));
                    map.put(KEY_SHAPE, cursor.getString(cursor
                            .getColumnIndexOrThrow("shape")));
                    dataList.add(map);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Occurred error",
                    Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            if (database != null)
                database.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        MenuUtils.addMenu(menu);

        menu.findItem(MenuUtils.MENU_SAVE).setVisible(false);
        menu.findItem(MenuUtils.MENU_SEARCH).setVisible(false);
        menu.findItem(MenuUtils.MENU_REFRESH_APP).setVisible(false);
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
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(listView));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(listView));
                ShareUtils.shareWithTwitter(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;
        }

        return true;
    }

}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.JobsActivity JD-Core Version: 0.6.0
 */