package com.project.material;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.material.helper.DatabaseHelper;
import com.project.material.helper.Fraction;
import com.project.material.util.CalculateVolumeUtils;
import com.project.material.util.CommonUtils;
import com.project.material.util.ShareUtils;
import com.project.material.util.image.ScreenCapture;
import com.project.material.util.menu.MenuUtils;
import com.project.material.util.rate.AppRater;
import com.project.material.widget.RowView;

public class CalculatorBasic extends SherlockActivity {

    public static final String[] CALCULATE_FUNCTIONS = {
            "calculateCone",
            "calculateCube",
            "calculateCylinder",
            "calculateHexagonalPrism",
            "calculateSphere",
            "calculateSquaredPyramid",
    };

    ///////////////
    public static final int[] SHAPE_IMAGE_RES = {
            R.drawable.shape_angle,
            R.drawable.shape_channel,
            R.drawable.shape_cone,
            R.drawable.shape_cube,
            R.drawable.shape_cyclinder,
            R.drawable.shape_hexagonal_prism,
            R.drawable.shape_i_beam,
            R.drawable.shape_rectangle_tube,
            R.drawable.shape_round_tube,
            R.drawable.shape_sphere,
            R.drawable.shape_square_based_pyramid,
            R.drawable.shape_square_tube,
            R.drawable.shape_tee_beam,
    };

    private int flag = 0;

    // View Definition
    private LinearLayout mLayoutTitle;
    private TextView mTextTitle;
    private Spinner mSpinnerType, mSpinnerMaterial, mSpinnerShape;
    private LinearLayout mLayoutInput;
    private ImageView mImageShapeDescription;
    private LinearLayout mLayoutAnswer;
    private TextView mTextAnswer;
    private LinearLayout mLayoutResult;

    // Variable
    private int mTitleHeight;
    private int mRowItemViewHeight;
    private ArrayAdapter<CharSequence> mAdapterType, mAdapterMaterial, mAdapterShape;
    private String[] mArrayMetricDensity;
    private String[] mArrayImperialDensity;

    // State variables
    private int mMode = RowView.METRIC;
    private int mMaterialIdx = 0;
    private int mShape = CalculateVolumeUtils.ANGLE;
    private int mInputParamCount = 4;

    // Variables for calculation
    private String mCalculateFunction;
    private ArrayList<Double> mInputParamList = new ArrayList<Double>();

    private View.OnClickListener mAnswerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkInputParameters())
                displayCalculationResults();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.activity_calculator_basic);

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

        initViews();
        initSpinners();
        initShapeDescription();
        initInputLayout();
        initAnswerLayout();
        initResultLayout();
    }

    @Override
    protected void onDestroy() {
        // closing Entire Application
        if (flag == 2) android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        flag = 1;
        //finalActions();
        showSaveConfirmationDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //MenuUtils.addMenu(menu);
        //menu.findItem(MenuUtils.MENU_SAVE).setVisible(true);
        //menu.findItem(MenuUtils.MENU_REFRESH_APP).setVisible(false);
        //menu.findItem(MenuUtils.MENU_SEARCH).setVisible(false);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MenuUtils.MENU_HOME:
                flag = 1;
                showSaveConfirmationDialog();
                break;

            case MenuUtils.MENU_SAVE:
                flag = 0;
                showSaveConfirmationDialog();
                break;

            case MenuUtils.MENU_REFRESH_APP:
                initInputLayout();
                initResultLayout();
                break;

            case MenuUtils.MENU_SEARCH:
                flag = 3;
                showSaveConfirmationDialog();
                break;

            case MenuUtils.MENU_RATE_US:
                AppRater.doRate(this);
                break;

            case MenuUtils.MENU_EXIT_APP:
                flag = 2;
                showSaveConfirmationDialog();
                break;

            case MenuUtils.MENU_FACEBOOK_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(mLayoutTitle));
                ShareUtils.shareWithFacebook(this,
                        String.format("%s, %s's Calculation result.",
                                mSpinnerMaterial.getSelectedItem().toString(),
                                mSpinnerShape.getSelectedItem().toString()),
                        ScreenCapture.CAPTURE_FILE_NAME
                );
                break;

            case MenuUtils.MENU_TWITTER_ITEM:
                ScreenCapture.createImageFromBitmap(ScreenCapture.getBitmapOfView(mLayoutTitle));
                ShareUtils.shareWithTwitter(this,
                        String.format("%s, %s's Calculation result.",
                                mSpinnerMaterial.getSelectedItem().toString(),
                                mSpinnerShape.getSelectedItem().toString()),
                        ScreenCapture.CAPTURE_FILE_NAME
                );
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShareUtils.TWITTER_LOGIN_CODE)
            ShareUtils.getAccessToken(this, data.getStringExtra(ShareUtils.PIN_CODE_EXTRA));
    }

    private void initViews() {
        mLayoutTitle = (LinearLayout) findViewById(R.id.layout_title);
        mTextTitle = (TextView) findViewById(R.id.text_title);

        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
        mSpinnerMaterial = (Spinner) findViewById(R.id.spinner_material);
        mSpinnerShape = (Spinner) findViewById(R.id.spinner_shape);

        mLayoutInput = (LinearLayout) findViewById(R.id.layout_input_value);

        mImageShapeDescription = (ImageView) findViewById(R.id.image_shape_description);

        mLayoutAnswer = (LinearLayout) findViewById(R.id.layout_answer);
        mTextAnswer = (TextView) findViewById(R.id.text_answer);

        mLayoutResult = (LinearLayout) findViewById(R.id.layout_result_value);

        // Scaling
        final Resources res = getResources();

        /*
        mTitleHeight = (int) (res.getDimension(R.dimen.title_text_view_height) * CommonUtils.mScaleYFactor);
        mRowItemViewHeight = (int) (res.getDimension(R.dimen.row_view_height) * CommonUtils.mScaleYFactor);
        float titleTextSize = res.getDimension(R.dimen.title_text_size) * CommonUtils.mFontScaleFactor;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLayoutTitle.getLayoutParams();
        params.height = mTitleHeight;
        mLayoutTitle.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mLayoutAnswer.getLayoutParams();
        params.height = mRowItemViewHeight;
        mLayoutAnswer.setLayoutParams(params);

        mTextTitle.setTextSize(titleTextSize);
        mTextAnswer.setTextSize(titleTextSize);
        */
        /*
        int spinnerHeight = (int) ((int) res.getDimension(R.dimen.spinner_height) * CommonUtils.mScaleYFactor);
        params = (LinearLayout.LayoutParams) mSpinnerType.getLayoutParams();
        params.height = spinnerHeight;
        mSpinnerType.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mSpinnerMaterial.getLayoutParams();
        params.height = spinnerHeight;
        mSpinnerMaterial.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mSpinnerShape.getLayoutParams();
        params.height = spinnerHeight;
        mSpinnerShape.setLayoutParams(params);
        */
    }

    private void initSpinners() {
        final Resources res = getResources();

        String[] array = res.getStringArray(R.array.type_array);
        mAdapterType = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, array);
        mAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        array = res.getStringArray(R.array.material_name_array);
        mAdapterMaterial = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, array);
        mAdapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        array = res.getStringArray(R.array.cubic_name_array);
        mAdapterShape = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, array);
        mAdapterShape.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mArrayImperialDensity = res.getStringArray(R.array.material_imperial_density_array);
        mArrayMetricDensity = res.getStringArray(R.array.material_metric_density_array);

        mSpinnerType.setAdapter(mAdapterType);
        mSpinnerMaterial.setAdapter(mAdapterMaterial);
        mSpinnerShape.setAdapter(mAdapterShape);

        mSpinnerType.setSelection(mMode);
        mSpinnerMaterial.setSelection(mMaterialIdx);
        mSpinnerShape.setSelection(mShape);

        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String title = mAdapterType.getItem(i).toString();
                if (title.contains("Metric"))
                    mMode = RowView.METRIC;
                else
                    mMode = RowView.IMPERIAL;
                initInputLayout();
                initResultLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMaterialIdx = i;
                initResultLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mShape != i) {
                    mShape = i;
                    initInputLayout();
                    initShapeDescription();
                    initAnswerLayout();
                    initResultLayout();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initShapeDescription() {
        mImageShapeDescription.setVisibility(View.VISIBLE);
        mImageShapeDescription.setImageResource(SHAPE_IMAGE_RES[mShape]);
    }

    private void initInputLayout() {
        mLayoutInput.removeAllViews();
        mTextTitle.setText(String.format("Material - Weights %s", mAdapterType.getItem(mMode)));

        HashMap<String, Object> attrs = new HashMap<String, Object>();
        RowView rowView;

        switch (mShape) {
            case CalculateVolumeUtils.ANGLE:
            case CalculateVolumeUtils.RECTANGLE_TUBE:
            case CalculateVolumeUtils.SQUARE_TUBE:
            case CalculateVolumeUtils.TEE_BEAM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Width");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Length");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "D. Gauge");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 4;
                break;

            case CalculateVolumeUtils.CHANNEL:
            case CalculateVolumeUtils.I_BEAM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Width");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Width");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "D. Length");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "E. Gauge");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 5;
                break;

            case CalculateVolumeUtils.CONE:
            case CalculateVolumeUtils.CYLINDER:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Radius");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 2;
                break;

            case CalculateVolumeUtils.CUBE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Width");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Length");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.SQUARE_BASED_PYRAMID:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Length");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Width");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.ROUND_TUBE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Diameter");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Gauge");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "C. Length");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 3;
                break;

            case CalculateVolumeUtils.HEXAGONAL_PRISM:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Side");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);

                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "B. Height");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 2;
                break;

            case CalculateVolumeUtils.SPHERE:
                attrs.clear();
                attrs.put(RowView.ROW_LABEL_STRING, "A. Radius");
                rowView = new RowView(this, mMode, attrs);
                mLayoutInput.addView(rowView);
                mInputParamCount = 1;
                break;
        }

    }

    private void initAnswerLayout() {
        mTextAnswer.setText(getUnderlineSpanString(getString(R.string.press_for_calculator)));
        mLayoutAnswer.setOnClickListener(mAnswerListener);
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

    private SpannableString getUnderlineSpanString(String string) {
        SpannableString content = new SpannableString(string);
        content.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        return content;
    }

    private boolean checkInputParameters() {
        mInputParamList.clear();

        for (int i = 0; i < mInputParamCount; i++) {
            RowView rowView = (RowView) mLayoutInput.getChildAt(i);
            String strLabel, strInput1, strInput2;
            double param1, param2;
            double dValue = 0.0d;

            strLabel = rowView.getLabel();
            if (strLabel.length() > 3)
                strLabel = strLabel.substring(3);

            // Check fields
            try {
                strInput1 = rowView.getValue1();
                param1 = Double.parseDouble(strInput1);

                if (TextUtils.isEmpty(strInput1) || param1 < 0 || param1 > 1000) {
                    Toast.makeText(CalculatorBasic.this,
                            String.format("Please Adjust %s", strLabel),
                            Toast.LENGTH_SHORT).show();
                    rowView.setEditValue1("");
                    return false;
                }

                rowView.setEditValue1(String.format("%.3f", param1));
                dValue = param1;

                if (mMode == RowView.IMPERIAL) {
                    rowView.setEditValue1(String.format("%d", (int) param1));

                    strInput2 = rowView.getValue2();
                    if (TextUtils.isEmpty(strInput2)) {
                        if (param1 != 0) {
                            param2 = 0;
                            rowView.setEditValue2("0");
                        } else {
                            Toast.makeText(CalculatorBasic.this,
                                    String.format("Please Adjust %s inches field", strLabel),
                                    Toast.LENGTH_SHORT).show();
                            rowView.setEditValue2("");
                            return false;
                        }
                    } else {
                        Fraction fraction = CommonUtils.getFractionFromString(
                                rowView.getValue2(), new Fraction(0, 0, 0), new Fraction(1000, 0, 0));
                        if (fraction == null) {
                            rowView.setEditValue2("");
                            Toast.makeText(CalculatorBasic.this,
                                    String.format("Please Adjust %s inches field", strLabel),
                                    Toast.LENGTH_SHORT).show();
                            rowView.setEditValue2("");
                            return false;
                        }

                        int feet = fraction.main / 12;
                        int inches = fraction.main % 12;

                        if (feet > 0) {
                            rowView.setEditValue1(String.valueOf((int) param1 + feet));
                            rowView.setEditValue2(CommonUtils.getFractionExpression(inches, fraction.up, 16));
                        }

                        param2 = (fraction.main * 16 + fraction.up) / 16.0;
                    }

                    //rowView.setEditValue2(String.format("%.3f", param2));
                    dValue += (param2 / 12.d);
                }
            } catch (Exception e) {
                Toast.makeText(CalculatorBasic.this,
                        String.format("Please Adjust %s", strLabel),
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            mInputParamList.add(dValue);
        }

        for (int i = mInputParamCount; i < CalculateVolumeUtils.MAX_PARAMETER_COUNT; i++)
            mInputParamList.add(1.0d);

        return true;
    }

    private void displayCalculationResults() {
        // Java method call indirectly
        //getClass().getMethod(CALCULATE_FUNCTIONS[mShape], Double.class, Double.class, Double.class);
        double value = CalculateVolumeUtils.calculateVolume(mShape,
                mInputParamList.get(0),
                mInputParamList.get(1),
                mInputParamList.get(2),
                mInputParamList.get(3),
                mInputParamList.get(4));

        RowView rowView = (RowView) mLayoutResult.getChildAt(0);
        rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / 1000.0f));
        if(mMode == RowView.METRIC)
        {
            if (mMode == RowView.METRIC)
                value = value * Double.parseDouble(mArrayMetricDensity[mMaterialIdx]);
            else
                value = value * Double.parseDouble(mArrayImperialDensity[mMaterialIdx]);

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
            rowView = (RowView) mLayoutResult.getChildAt(1);//add
            rowView.setResultText(String.format("%.3f", Math.round(value * 1000) / (1000.0f * 27)));

            if (mMode == RowView.METRIC)
                value = value * Double.parseDouble(mArrayMetricDensity[mMaterialIdx]);
            else
                value = value * Double.parseDouble(mArrayImperialDensity[mMaterialIdx]);

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

    public void finalActions() {
        switch (flag) {
            case 1:
                finish();
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
                break;
            case 2:
                //CommonUtils.gotoHome(this);
                finish();
                break;
            case 3:
                finish();
                startActivity(new Intent(this, JobMenuActivity.class));
                overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
                break;
        }
    }

    public void showSaveConfirmationDialog() {
        RowView rowView = (RowView) mLayoutResult.getChildAt(0);

        if (TextUtils.isEmpty(rowView.getValue1())) {
            finalActions();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Would you like to save calculator answers?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSaveNameDialog();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalActions();
                    }
                }).show();
    }

    public void showSaveNameDialog() {

        final EditText editName = new EditText(CalculatorBasic.this);
        editName.setMaxLines(1);

        new AlertDialog.Builder(CalculatorBasic.this)
                .setTitle("Job Details")
                .setMessage("Please enter job name.")
                .setView(editName)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (TextUtils.isEmpty(editName.getText().toString())) {
                            Toast.makeText(CalculatorBasic.this,
                                    "Can not save the data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SQLiteDatabase database = null;
                        try {
                            database = new DatabaseHelper(CalculatorBasic.this, getString(R.string.app_name), null, 1)
                                    .getWritableDatabase();
                            ContentValues contentValues = new ContentValues();

                            contentValues.put("title", editName.getText().toString());
                            contentValues.put("date", "");
                            contentValues.put("name", "");
                            contentValues.put("address", "");
                            contentValues.put("phone", "");
                            contentValues.put("email", "");
                            contentValues.put("margin", "");
                            contentValues.put("tax", "");
                            contentValues.put("method", String.valueOf(mMode));
                            if (mMode == RowView.METRIC)
                                contentValues.put("value", mArrayMetricDensity[mMaterialIdx]);
                            else
                                contentValues.put("value", mArrayImperialDensity[mMaterialIdx]);
                            contentValues.put("option", mSpinnerMaterial.getSelectedItem().toString());
                            contentValues.put("shape", String.valueOf(mShape));

                            System.out.println(mSpinnerMaterial.getSelectedItem().toString());

                            contentValues.put("param1", String.valueOf(mInputParamList.get(0)));
                            contentValues.put("param2", String.valueOf(mInputParamList.get(1)));
                            contentValues.put("param3", String.valueOf(mInputParamList.get(2)));
                            contentValues.put("param4", String.valueOf(mInputParamList.get(3)));
                            contentValues.put("param5", String.valueOf(mInputParamList.get(4)));

                            database.insert("jobstable", null, contentValues);
                            database.close();

                            finalActions();
                        } catch (Exception e) {
                            Toast.makeText(CalculatorBasic.this, "Occurred error while saving data",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finalActions();
                    }
                }).show();
    }

}
