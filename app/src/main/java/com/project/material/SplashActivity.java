package com.project.material;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.project.material.helper.DatabaseHelper;
import com.project.material.helper.GlobalVariables;
import com.project.material.util.CommonUtils;

public class SplashActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    private Boolean isEnding = false;

    // The thread to wait for splash screen events
    private final Thread mSplashThread = new Thread() {
        @Override
        public void run() {
            try {
                synchronized (this) {
                    // Wait given period of time or exit on touch
                    wait(5000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (isEnding)
                return;

            // Run next activity
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

            finish();
            this.interrupt();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CommonUtils.calculateScaleFactor(this);
        getSettings();

        // Splash screen view
        setContentView(R.layout.activity_splash);

		/*
         * view = (TextView)findViewById(R.id.textView2); String mystring=new
		 * String("Paving Calculator"); SpannableString edtContent = new
		 * SpannableString(mystring); edtContent.setSpan(new UnderlineSpan(), 0,
		 * mystring.length(), 0); view.setText(edtContent);
		 */

        mSplashThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isEnding = true;
        synchronized (mSplashThread) {
            mSplashThread.notify();
        }
    }

    private void getSettings() {
        SQLiteDatabase sqliteDatabase = null;

        try {
            sqliteDatabase = new DatabaseHelper(this,
                    getString(R.string.app_name), null, 1)
                    .getWritableDatabase();
            if (sqliteDatabase == null)
                return;

            Cursor cursor = sqliteDatabase.query("settings", null, null, null,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (true) {
                    if (cursor.isAfterLast()) {
                        cursor.close();
                        sqliteDatabase.close();
                        break;
                    }
                    GlobalVariables.unit = cursor.getInt(0);
                    cursor.moveToNext();
                }
            }

            if (cursor != null)
                cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (sqliteDatabase != null)
                sqliteDatabase.close();
        }
    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.SplashActivity JD-Core Version: 0.6.0
 */