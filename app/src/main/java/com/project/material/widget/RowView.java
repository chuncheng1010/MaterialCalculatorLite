package com.project.material.widget;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.material.helper.Fraction;
import com.project.material.util.CommonUtils;

public class RowView extends LinearLayout {

    // ===========================================================
    // Constants
    // ===========================================================

    /**
     * Only allow the user to input on one EditText by Metre unit.
     */
    public static final int METRIC = 0;
    private int mMode = METRIC;
    /**
     * Allow the user to input on two EditText by Feet and Inch unit.
     */
    public static final int IMPERIAL = 1;
    /**
     * Allow the user not to input EditText.
     */
    public static final int RESULT = 2;
    public static final String ROW_MODE = "row_mode";
    public static final String ROW_BACK_COLOR = "row_back_color";
    public static final String ROW_EDIT_BACK_COLOR = "row_edit_back_color";
    public static final String ROW_LABEL_TEXT_COLOR = "row_label_text_color";
    public static final String ROW_EDIT_TEXT_COLOR = "row_edit_text_color";
    public static final String ROW_LABEL_STRING = "row_label_string";
    public static final String ROW_EDIT1_STRING = "row_edit1_string";
    public static final String ROW_EDIT2_STRING = "row_edit2_string";
    public static final String ROW_UNIT_STRING = "row_unit_string";
    static final boolean DEBUG = true;
    static final String LOG_TAG = RowView.class.getSimpleName();
    private static final String STATE_MODE = "row_mode";

    // ===========================================================
    // Fields
    // ===========================================================

    private static final String STATE_SUPER = "row_super";
    private boolean mCanEdit = true;

    private String mStrLabel = "";
    private String mStrEdit1 = "";
    private String mStrEdit2 = "";
    private String mStrUnit = "";

    private TextView mTextLabel;
    private TextView mTextUnit1;
    private TextView mTextUnit2;
    private EditText mEditValue1;
    private EditText mEditValue2;


    // ===========================================================
    // Constructors
    // ===========================================================

    public RowView(Context context, int mode, HashMap<String, Object> attrs) {
        super(context);
        mMode = mode;
        init(context, attrs);
    }

    public RowView(Context context, int mode, HashMap<String, Object> attrs, boolean canEdit) {
        super(context);
        mMode = mode;
        mCanEdit = canEdit;
        init(context, attrs);
    }

    @Override
    protected final void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            mMode = bundle.getInt(STATE_MODE);

            // Let super Restore Itself
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putInt(STATE_MODE, mMode);
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());

        return bundle;
    }

    private void init(Context context, HashMap<String, Object> attrs) {

        if (attrs != null) {
            parseStyleable(attrs);
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.height = (int) (context.getResources().getDimension(R.dimen.row_view_height) * CommonUtils.mScaleYFactor);
        params.topMargin = 5;
        setLayoutParams(params);

        //mLabelTextSize = context.getResources().getDimension(R.dimen.row_view_label_text_size) * CommonUtils.mFontScaleFactor;
        //mEditTextSize = context.getResources().getDimension(R.dimen.row_view_edit_text_size) * CommonUtils.mFontScaleFactor;

        // TextView for Label
        mTextLabel = new TextView(context);
        mTextLabel.setText(mStrLabel);
        mTextLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        //mTextLabel.setTextSize(mLabelTextSize);
        mTextLabel.setTextColor(Color.WHITE);
        mTextLabel.setPadding((int) (20 * CommonUtils.mScaleXFactor), 0, 0, 0);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 2;
        mTextLabel.setLayoutParams(params);
        addView(mTextLabel);

        // EditText for Value1
        mEditValue1 = new EditText(context);
        mEditValue1.setText(mStrEdit1);
        mEditValue1.setGravity(Gravity.CENTER);
        mEditValue1.setSingleLine();
        mEditValue1.setMaxEms(3);
        mEditValue1.setFocusable(mCanEdit);
        mEditValue1.setFocusableInTouchMode(mCanEdit);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mMode == METRIC) {
            params.weight = 2;
            mEditValue1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (mMode == IMPERIAL) {
            params.weight = 1;
            mEditValue1.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (mMode == RESULT) {
            params.weight = 2;
            mEditValue1.setFocusable(false);
            mEditValue1.setFocusableInTouchMode(false);
            mEditValue1.setText(mStrEdit1);
        }

        //params.topMargin = 5;
        //params.bottomMargin = 5;
        mEditValue1.setLayoutParams(params);
        addView(mEditValue1);

        // TextView for Unit1
        mTextUnit1 = new TextView(context);
        mTextUnit1.setText(mStrUnit);
        mTextUnit1.setGravity(Gravity.CENTER);
        mTextUnit1.setTextColor(Color.WHITE);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mMode == METRIC) {
            params.weight = 2;
            mTextUnit1.setText("Metres");
        } else if (mMode == IMPERIAL) {
            params.weight = 1;
            mTextUnit1.setText("Feet");
        } else if (mMode == RESULT) {
            params.weight = 2;
            mTextUnit1.setFocusable(false);
            mTextUnit1.setText(mStrUnit);
        }

        mTextUnit1.setLayoutParams(params);
        addView(mTextUnit1);

        // EditText for Value2
        mEditValue2 = new EditText(context);
        mEditValue2.setText(mStrEdit2);
        mEditValue2.setGravity(Gravity.CENTER);
        mEditValue2.setEms(10);
        mEditValue2.setSingleLine();
        mEditValue2.setFocusable(mCanEdit);
        mEditValue2.setFocusableInTouchMode(mCanEdit);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mMode == METRIC) {
            params.weight = 1;
            mEditValue2.setVisibility(View.GONE);
            mEditValue2.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (mMode == IMPERIAL) {
            params.weight = 1;
            mEditValue2.setVisibility(View.VISIBLE);
            mEditValue2.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (mMode == RESULT) {
            mEditValue2.setVisibility(View.GONE);
        }

        //params.topMargin = 5;
        //params.bottomMargin = 5;
        mEditValue2.setLayoutParams(params);
        addView(mEditValue2);

        // TextView for Unit2
        mTextUnit2 = new TextView(context);
        mTextUnit2.setText(mStrUnit);
        mTextUnit2.setGravity(Gravity.CENTER);
        mTextUnit2.setTextColor(Color.WHITE);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mMode == METRIC) {
            params.weight = 1;
            mTextUnit2.setVisibility(View.GONE);
        } else if (mMode == IMPERIAL) {
            params.weight = 1;
            mTextUnit2.setText("Inches");
        } else if (mMode == RESULT) {
            mTextUnit2.setVisibility(View.GONE);
        }

        mTextUnit2.setLayoutParams(params);
        addView(mTextUnit2);
    }

    public String getLabel() {
        return mTextLabel.getText().toString();
    }

    public String getValue1() {
        return mEditValue1.getText().toString();
    }

    public String getValue2() {
        return mEditValue2.getText().toString();
    }

    public void setLabelText(String label) {
        mTextLabel.setText(label);
    }

    public void setEditValue1(String value) {
        mEditValue1.setText(value);
    }

    public void setTextUnit1(String unit) {
        mTextUnit1.setText(unit);
    }

    public void setEditValue2(String value) {
        mEditValue2.setText(value);
    }

    public void setTextUnit2(String unit) {
        mTextUnit2.setText(unit);
    }

    public void setResultText(String result) {
        mEditValue1.setText(result);
    }

    public void setInputValue(String input) {
        if (mMode == METRIC) {
            mEditValue1.setText(input);
        } else if (mMode == IMPERIAL) {
            double feet = Double.parseDouble(input);
            mEditValue1.setText(String.valueOf((int) feet));
            double inches = (feet - (int) feet) * 12;
            Fraction fraction = CommonUtils.getFractionFromString(
                    String.valueOf(inches), new Fraction(0, 0, 0), new Fraction(1000, 0, 0));
            mEditValue2.setText(CommonUtils.getFractionExpression(fraction.main, fraction.up, 16));
        }
    }

    private void parseStyleable(HashMap<String, Object> attrs) {

        if (attrs.containsKey(ROW_MODE)) {
            mMode = (Integer) attrs.get(ROW_MODE);
        }
        if (attrs.containsKey(ROW_LABEL_STRING)) {
            mStrLabel = (String) attrs.get(ROW_LABEL_STRING);
        }
        if (attrs.containsKey(ROW_EDIT1_STRING)) {
            mStrEdit1 = (String) attrs.get(ROW_EDIT1_STRING);
        }
        if (attrs.containsKey(ROW_EDIT2_STRING)) {
            mStrEdit2 = (String) attrs.get(ROW_EDIT2_STRING);
        }
        if (mMode == RESULT && attrs.containsKey(ROW_UNIT_STRING)) {
            mStrUnit = (String) attrs.get(ROW_UNIT_STRING);
        }
    }

}
