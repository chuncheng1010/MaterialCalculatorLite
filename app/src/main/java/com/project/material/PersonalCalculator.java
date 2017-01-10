package com.project.material;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.material.helper.DatabaseHelper;
import com.project.material.util.CalculateVolumeUtils;
import com.project.material.util.ShareUtils;
import com.project.material.util.image.ScreenCapture;
import com.project.material.util.menu.MenuUtils;
import com.project.material.util.rate.AppRater;
import com.project.material.widget.RowView;

public class PersonalCalculator extends SherlockActivity {

    // View Definition
    private LinearLayout mLayoutInput;
    private ImageView mImageShapeDescription;
    private LinearLayout mLayoutResult;

    // State variables
    private String mID;
    private int mMode = RowView.METRIC;
    private int mMaterialIdx = 0;
    private int mShape = CalculateVolumeUtils.ANGLE;
    private int mInputParamCount = 4;
    private String mMaterialDensity;// value
    private String mMaterialName;// option

    // Variables for calculation
    private ArrayList<Double> mInputParamList = new ArrayList<Double>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_personal_calculations);

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


        mID = getIntent().getStringExtra("value");
        mMode = Integer.parseInt(getIntent().getStringExtra("method"));
        mMaterialDensity = getIntent().getStringExtra("spinnerMaterial");
        mMaterialName = getIntent().getStringExtra("option");
        mShape = Integer.parseInt(getIntent().getStringExtra("shape"));

        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = new DatabaseHelper(this,
                    getString(R.string.app_name), null, 1).getWritableDatabase();

            cursor = database.query("jobstable", new String[]{
                            "id", "param1", "param2", "param3", "param4", "param5",
                            "option", "value", "method",
                            "name", "phone", "email", "address", "date"},
                    "id=?", new String[]{mID}, null, null, null
            );

            mInputParamList.clear();
            mInputParamCount = 0;

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                mMaterialName = cursor.getString(cursor
                        .getColumnIndexOrThrow("option"));
                mMaterialDensity = cursor.getString(cursor
                        .getColumnIndexOrThrow("value"));
                mMode = Integer.parseInt(cursor.getString(cursor
                        .getColumnIndexOrThrow("method")));

                mInputParamList.add(Double.parseDouble(cursor.getString(cursor
                        .getColumnIndexOrThrow("param1"))));
                mInputParamCount++;
                mInputParamList.add(Double.parseDouble(cursor.getString(cursor
                        .getColumnIndexOrThrow("param2"))));
                mInputParamCount++;
                mInputParamList.add(Double.parseDouble(cursor.getString(cursor
                        .getColumnIndexOrThrow("param3"))));
                mInputParamCount++;
                mInputParamList.add(Double.parseDouble(cursor.getString(cursor
                        .getColumnIndexOrThrow("param4"))));
                mInputParamCount++;
                mInputParamList.add(Double.parseDouble(cursor.getString(cursor
                        .getColumnIndexOrThrow("param5"))));
                mInputParamCount++;
            }

            if (cursor != null) cursor.close();
        } catch (Exception e) {
            if (mInputParamCount < CalculateVolumeUtils.MAX_PARAMETER_COUNT) {
                for (int i = mInputParamCount; i <= CalculateVolumeUtils.MAX_PARAMETER_COUNT; i++) {
                    mInputParamList.add(1.0d);
                }
            } else {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Occurred error", Toast.LENGTH_SHORT).show();
            }
        } finally {
            database.close();
        }

        initViews();
        initShapeDescription();
        initInputLayout();
        initResultLayout();

        displayCalculationResults();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, JobsActivity.class));
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMaterialDensity = savedInstanceState.getString("value");
        mMode = savedInstanceState.getInt("method");
        mMaterialDensity = savedInstanceState.getString("spinnerMaterial");
        mMaterialName = savedInstanceState.getString("option");
        mShape = savedInstanceState.getInt("shape");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", mMaterialDensity);
        outState.putInt("method", mMode);
        outState.putString("spinnerMaterial", mMaterialDensity);
        outState.putString("option", mMaterialName);
        outState.putInt("shape", mShape);
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
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(mLayoutInput));
                ShareUtils.shareWithFacebook(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(mLayoutInput));
                ShareUtils.shareWithTwitter(this, "", ScreenCapture.CAPTURE_FILE_NAME);
                break;
        }

        return true;
    }

    private void initViews() {
        mLayoutInput = (LinearLayout) findViewById(R.id.layout_input_value);
        mImageShapeDescription = (ImageView) findViewById(R.id.image_shape_description);
        mLayoutResult = (LinearLayout) findViewById(R.id.layout_result_value);
    }

    private void initShapeDescription() {
        mImageShapeDescription.setVisibility(View.VISIBLE);
        mImageShapeDescription.setImageResource(CalculatorBasic.SHAPE_IMAGE_RES[mShape]);
    }

    private void initInputLayout() {
        ((TextView) findViewById(R.id.text_material)).setText(mMaterialName);

        mLayoutInput.removeAllViews();

        HashMap<String, Object> attrs = new HashMap<String, Object>();
        RowView rowView;

        switch (mShape) {
            case CalculateVolumeUtils.ANGLE:
            case CalculateVolumeUtils.RECTANGLE_TUBE:
            case CalculateVolumeUtils.SQUARE_TUBE:
            case CalculateVolumeUtils.TEE_BEAM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Width");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Length");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(2)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "D. Gauge");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(3)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 4;
                break;

            case CalculateVolumeUtils.CHANNEL:
            case CalculateVolumeUtils.I_BEAM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Width");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Width");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(2)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "D. Length");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(3)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "E. Gauge");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(4)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 5;
                break;

            case CalculateVolumeUtils.CONE:
            case CalculateVolumeUtils.CYLINDER:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Radius");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 2;
                break;

            case CalculateVolumeUtils.CUBE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Width");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Length");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(2)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.SQUARE_BASED_PYRAMID:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Length");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Width");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(2)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.ROUND_TUBE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Diameter");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Gauge");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Length");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(2)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.HEXAGONAL_PRISM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Side");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Height");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(1)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 2;
                break;

            case CalculateVolumeUtils.SPHERE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Radius");
                rowView = new RowView(this, mMode, attrs, false);
                rowView.setInputValue(String.valueOf(mInputParamList.get(0)));
                mLayoutInput.addView(rowView);
                mInputParamCount = 1;
                break;
        }

    }

    private void initResultLayout() {
        mLayoutResult.removeAllViews();

        HashMap<String, Object> attrs = new HashMap<String, Object>();
        RowView rowView;

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Total Volume");
        if (mMode == RowView.METRIC)
            attrs.put(RowView.ROW_UNIT_STRING, String.format("M%c", 179));
        else
            attrs.put(RowView.ROW_UNIT_STRING, String.format("FT%c", 179));
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);

        if(mMode == RowView.IMPERIAL)
        {
            attrs.clear();
            attrs.put(RowView.ROW_LABEL_STRING, "Total Volume");

            attrs.put(RowView.ROW_UNIT_STRING, String.format("Yd%c", 179));
            rowView = new RowView(this, RowView.RESULT, attrs);
            mLayoutResult.addView(rowView);
        }

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Weight");
        attrs.put(RowView.ROW_UNIT_STRING, "tones");
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Weight");
        attrs.put(RowView.ROW_UNIT_STRING, "stone");
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Weight");
        attrs.put(RowView.ROW_UNIT_STRING, "kilos");
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Weight");
        attrs.put(RowView.ROW_UNIT_STRING, "pounds");
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);

        attrs.clear();
        attrs.put(RowView.ROW_LABEL_STRING, "Weight");
        attrs.put(RowView.ROW_UNIT_STRING, "ounces");
        rowView = new RowView(this, RowView.RESULT, attrs);
        mLayoutResult.addView(rowView);
    }

    private void displayCalculationResults() {
        double value = CalculateVolumeUtils.calculateVolume(mShape,
                mInputParamList.get(0),
                mInputParamList.get(1),
                mInputParamList.get(2),
                mInputParamList.get(3),
                mInputParamList.get(4));

        if(mMode == RowView.METRIC) {
            RowView rowView = (RowView) mLayoutResult.getChildAt(0);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = value * Double.parseDouble(mMaterialDensity);

            rowView = (RowView) mLayoutResult.getChildAt(3);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            double kilos = value;
            value = kilos / 1000.0f;
            rowView = (RowView) mLayoutResult.getChildAt(1);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 0.157473;
            rowView = (RowView) mLayoutResult.getChildAt(2);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 2.20462;
            rowView = (RowView) mLayoutResult.getChildAt(4);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 35.274;
            rowView = (RowView) mLayoutResult.getChildAt(5);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));
        }
        else
        {
            RowView rowView = (RowView) mLayoutResult.getChildAt(0);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            rowView = (RowView) mLayoutResult.getChildAt(1);
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / (1000.0f * 27)));

            value = value * Double.parseDouble(mMaterialDensity);

            rowView = (RowView) mLayoutResult.getChildAt(4);//3
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            double kilos = value;
            value = kilos / 1000.0f;
            rowView = (RowView) mLayoutResult.getChildAt(2);//1
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 0.157473;
            rowView = (RowView) mLayoutResult.getChildAt(3);//2
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 2.20462;
            rowView = (RowView) mLayoutResult.getChildAt(5);//4
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));

            value = kilos * 35.274;
            rowView = (RowView) mLayoutResult.getChildAt(6);//5
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));
        }
    }

}
