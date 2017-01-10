package com.project.material;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

public class NoteListActivity extends SherlockActivity {

    private EditText edtContent;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle paramBundle) {

        super.onCreate(paramBundle);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_note_list);

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


        edtContent = (EditText) findViewById(R.id.editText1);
        edtContent.setTextSize(CommonUtils.mNoteTextSize);

        String WRITE_FILE = "edtContent.txt";
        /*
        File file = new File(WRITE_FILE);
        int size = (int) file.length();

        if (size != 0) {
            byte[] buf = new byte[size];
            BufferedInputStream bufferedInputStream;
        */
            try {
                /*
                bufferedInputStream = new BufferedInputStream(
                        new FileInputStream(file));
                bufferedInputStream.read(buf, 0, size);
                bufferedInputStream.close();
                */
                FileInputStream in = openFileInput(WRITE_FILE);
                byte[] buf = new byte[in.available()];
                while(in.read(buf) != -1);
                edtContent.setText(new String(buf));
                //edtContent.setText(new String(buf));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
    }

    @Override
    public void onBackPressed() {
        flag = 1;
        showSaveConfirmDialog();
    }

    private void processExit(int flag, boolean isSave) {
        if (isSave) {
            String fileName = "edtContent.txt";
            try {
                File f1 = getFileStreamPath(fileName);
                f1.createNewFile();
                FileOutputStream fos1 = new FileOutputStream(f1);
                fos1.write(edtContent.getText().toString().getBytes());
                fos1.close();
            } catch (Exception e) {
                Toast.makeText(NoteListActivity.this, "Can't find file",
                        Toast.LENGTH_SHORT).show();
            }
        }

        switch (flag) {
            case 1:
            case 3:
                finish();
                startActivity(new Intent(this, JobMenuActivity.class));
                overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
                break;
            case 2:
                finish();
                break;
        }
    }

    private void showSaveConfirmDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Would you like to save this note?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                processExit(flag, true);
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                processExit(flag, false);
                            }
                        }
                ).show();
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
                flag = 1;
                showSaveConfirmDialog();
                break;

            case MenuUtils.MENU_GOTO_PRO:
                AppRater.gotoProVersion(this);
                break;

            case MenuUtils.MENU_RATE_US:
                AppRater.doRate(this);
                break;

            case MenuUtils.MENU_EXIT_APP:
                flag = 2;
                showSaveConfirmDialog();
                break;

            case MenuUtils.MENU_FACEBOOK_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(edtContent));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(edtContent));
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