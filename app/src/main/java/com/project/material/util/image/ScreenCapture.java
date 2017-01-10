package com.project.material.util.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

public class ScreenCapture {

    public static final String CAPTURE_FILE_NAME = Environment.getExternalStorageDirectory() + "/" + "captured_screen.png";

    /*
     * @author : Mayur Sharma
     * This method is used to create the bitmap of the current activity
     * This method accepts any child view of the current view
     * You can even pass the parent container like RelativeLayout or LinearLayout as a param
     * @param : View v
     */
    public static Bitmap getBitmapOfView(View v) {
        View rootView = v.getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bmp = rootView.getDrawingCache();
        return bmp;
    }

    /*
     * @author : Mayur Sharma
     * This method is used to create an image file using the bitmap
     * This method accepts an object of Bitmap class
     * Currently we are passing the bitmap of the root view of current activity
     * The image file will be created by the name capturedscreen.jpg
     * @param : Bitmap bmp
     */
    public static void createImageFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, bytes);
        File file = new File(CAPTURE_FILE_NAME);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            ostream.write(bytes.toByteArray());
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
