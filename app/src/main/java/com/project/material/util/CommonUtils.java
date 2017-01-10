package com.project.material.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.project.material.R;
import com.project.material.helper.Fraction;

public class CommonUtils {

    public static float mScaleXFactor = 1.0f;
    public static float mScaleYFactor = 1.0f;
    public static float mMaxScaleFactor = 1.0f;
    public static float mMinScaleFactor = 1.0f;

    public static float mFontScaleFactor = 1.0f;

    public static float mItemButtonTextSize;
    public static float mNoteTextSize;
    public static float mWebViewTextSize;

    public static void calculateScaleFactor(Context context) {
        final Resources resources = context.getResources();

        float design_width = resources
                .getDimension(R.dimen.design_screen_width);
        float design_height = resources
                .getDimension(R.dimen.design_screen_height);

        DisplayMetrics metrics = resources.getDisplayMetrics();
        mScaleXFactor = metrics.widthPixels
                / (design_width * metrics.scaledDensity);
        mScaleYFactor = metrics.heightPixels
                / (design_height * metrics.scaledDensity);
        mFontScaleFactor = mScaleYFactor / metrics.density;

        mMaxScaleFactor = Math.max(mScaleXFactor, mScaleYFactor);
        mMinScaleFactor = Math.min(mScaleXFactor, mScaleYFactor);

        mItemButtonTextSize = resources
                .getDimension(R.dimen.item_button_text_size) * mFontScaleFactor;
        mNoteTextSize = resources.getDimension(R.dimen.note_text_size)
                * mFontScaleFactor;
        mWebViewTextSize = resources.getDimension(R.dimen.web_view_text_size)
                * mFontScaleFactor;
    }

    public static void onAbout(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context
                .getString(R.string.about))));
    }

    public static void onContactUs(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context
                .getString(R.string.contact_us))));
    }

    public static void onTwitter(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context
                .getString(R.string.twitter_link))));
    }

    public static void gotoHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Convert dpi to pixel
    public static int dpi2Pixel(Context context, int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5);
    }

    // Convert fraction and decimal expression to Fraction
    public static Fraction getFractionFromString(String strValue, Fraction min, Fraction max) {
        int main, up, down;
        double value;
        String temp;

        Pattern pattern = Pattern.compile("(^[0-9]+)\\p{Space}([0-9]+)/([1-9]+)$");
        Matcher matcher = pattern.matcher(strValue);

        if (matcher.find()) {
            main = Integer.parseInt(matcher.group(1));
            up = Integer.parseInt(matcher.group(2));
            down = Integer.parseInt(matcher.group(3));
            main += up / down;
            value = up / (down * 1.0);
            down = 16;
            up = (int) Math.round(value * 16);
            //System.out.println(main + " " + up + " " + down);
            if (main * 16 + up < min.main * 16 + min.up)
                return null;
            if (main * 16 + up > max.main * 16 + max.up)
                return null;

        } else {
            pattern = Pattern.compile("(^[0-9]+)\\.([0-9]+)$");
            matcher = pattern.matcher(strValue);

            if (matcher.find()) {
                main = Integer.parseInt(matcher.group(1));
                temp = String.format("%s.%s", matcher.group(1), matcher.group(2));
                value = Double.parseDouble(temp);
                value = value - main;
                down = 16;
                up = (int) Math.round(value * 16);
                //System.out.println(main + " " + up + " " + down);
            } else {
                pattern = Pattern.compile("(^[0-9]+)/([1-9]+)$");
                matcher = pattern.matcher(strValue);

                if (matcher.find()) {
                    main = Integer.parseInt(matcher.group(1)) / Integer.parseInt(matcher.group(2));
                    up = Integer.parseInt(matcher.group(1)) - main * Integer.parseInt(matcher.group(2));
                    value = (up * 1.0) / (Integer.parseInt(matcher.group(2)) * 1.0);
                    down = 16;
                    up = (int) Math.round(value * 16);
                } else {
                    pattern = Pattern.compile("(^[0-9]+)$");
                    matcher = pattern.matcher(strValue);
                    if (matcher.find()) {
                        main = Integer.parseInt(matcher.group(0));
                        up = 0;
                        down = 16;
                    } else
                        return null;
                }
            }
        }

        System.out.println(main + " " + up + " " + down);
        if (main * 16 + up < min.main * 16 + min.up)
            return null;
        if (main * 16 + up > max.main * 16 + max.up)
            return null;

        return new Fraction(main, up, down);
    }

    public static String getFractionExpression(int m, int u, int d) {
        if (m == 0 && u == 0)
            return String.format("%d", 0);
        if (m != 0 && u != 0)
            return String.format("%d %d/%d", m, u, 16);
        else if (m == 0)
            return String.format("%d/%d", u, 16);
        else
            return String.format("%d", m);
    }

}
