/**
 * @author Ry
 * @date 2013.08.3
 * @filename ShareUtils.java
 */

package com.project.material.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.android.social.TwitterLoginActivity;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.project.material.R;

public class ShareUtils {

    public static final boolean DEBUG = true;

    // Facebook
    public static final String FACEBOOK_APP_ID = "708134409209062";//"259432000846906"

    // Twitter
    public static final String TWITTER_CONSUMER_KEY = "zYGKEkqAEqfWPSr1NGbtkw";
    public static final String TWITTER_CONSUMER_SECRET = "Vb3imNJH7Yc1CqiG5dNwUnyrKC63F7uf0lOhMwAPhTc";
    public static final String TWITTER_CALLBACK_URL = ""; // You need to set this if using OAuth, see note above (xAuth users can skip it)
    public static final String PREF_NAME = "MaterialWeightCalculator";
    public static final String TWITTER_AUTHOR_URL = "https://api.twitter.com/oauth/authorize";

    ////////////////////////////////////////////////////////////////////////////////
    // email
    ////////////////////////////////////////////////////////////////////////////////
    public static final int TWITTER_LOGIN_CODE = 10001;
    public static final String PIN_CODE_EXTRA = "pin_code";

    ////////////////////////////////////////////////////////////////////////////////
    // Facebook
    ////////////////////////////////////////////////////////////////////////////////
    private final static String TWITPIC_API_KEY = "1861dd76c50a5b8e7e89eb8f2f504236";
    private final static String TWITTER_ACCESS_TOKEN = "Access_Token";
    private final static String TWITTER_ACCESS_TOKEN_SECRET = "Access_Token_Secret";
    public static Facebook mFacebook;
    public static AsyncFacebookRunner mAsyncRunner;
    public static Twitter mTwitter;
    public static RequestToken mRT;
    private static Activity sActivity;
    private static String mTitle;
    private static String mImgPath;
    private static Dialog mProgressDialog = null;
    private static String mImagePathForUploading;

    public static void sendMail(Context context, String text) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String[] address = {""};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        //emailIntent.setType("message/rfc822");
        //emailIntent.setType("image/gif");
        emailIntent.setType("text/html");

        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void sendMail(Context context, String dstAddr,
                                String subject, String text) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String[] addr = {dstAddr};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addr);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void shareWithFacebook(Activity activity, String title, String imagePath) {
        sActivity = activity;
        mTitle = title;

        postToFacebook(imagePath);
    }

    private static void initFacebook() {
        mFacebook = new Facebook(FACEBOOK_APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        SessionStore.restore(mFacebook, sActivity);
        SessionListener listener = new SessionListener();
        SessionEvents.addAuthListener(listener);
        SessionEvents.addLogoutListener(listener);
    }

    private static void logout() {
        try {
            mFacebook.logout(sActivity);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postToFacebook(String imgPath) {
        mImgPath = imgPath;
        if (mFacebook == null)
            initFacebook();
        if (mFacebook.isSessionValid()) {
            uploadBitmap();
        } else {
            mFacebook.authorize(sActivity, new String[]{"publish_stream", "read_stream", "offline_access"},
                    new LoginDialogListener());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Twitter
    ////////////////////////////////////////////////////////////////////////////////

    private static void uploadBitmap() {
        initProgressDialog(sActivity);
        showProgress();

        Bundle params = new Bundle();
        params.putString("method", "photos.upload");
        params.putString("caption", String.format("%s\n%s%s\n%s",
                sActivity.getString(R.string.app_name),
                sActivity.getString(R.string.app_url),
                sActivity.getPackageName(), mTitle));

        /*
        // From Asset
        AssetManager assetManager = sActivity.getAssets();
        try {
            long len = assetManager.openFd(mImgPath).getLength();
            AssetInputStream ais = (AssetInputStream) assetManager.open(mImgPath);
            byte[] imgData = new byte[(int) len];
            ais.read(imgData);
            params.putByteArray("picture", imgData);
            ais.close();
            mAsyncRunner.request(null, params, "POST", new ImageUploadListener(), null);
        } catch (FileNotFoundException e) {
            if (DEBUG) e.printStackTrace();
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
        }*/

        /*
        // From Drawable
        Bitmap image = BitmapFactory.decodeResource(
                sActivity.getResources(),
                sActivity.getResources().getIdentifier("ic_launcher", "drawable", sActivity.getPackageName()));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imgData = stream.toByteArray();
        params.putByteArray("picture", imgData);

        mAsyncRunner.request(null, params, "POST", new ImageUploadListener(), null);*/

        // From external file
        File file = new File(mImgPath);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] imgData = new byte[(int) file.length()];
            fis.read(imgData);
            params.putByteArray("picture", imgData);
            fis.close();
            mAsyncRunner.request(null, params, "POST", new ImageUploadListener(), null);
        } catch (FileNotFoundException e) {
            if (DEBUG) e.printStackTrace();
            dismissProgress();
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            dismissProgress();
        }
    }

    private static void showSuccessText() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sActivity, "Success Photo Uploading!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showFailText() {
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sActivity, "Fail Photo Uploading!",
                        Toast.LENGTH_LONG).show();
            }
        });

        logout();
    }

    /**
     * Initialize progress dialog
     */
    public static void initProgressDialog(Context context) {
        mProgressDialog = new Dialog(context, android.R.style.Theme_Translucent);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setContentView(R.layout.loading);
        mProgressDialog.setCancelable(false);
    }

    /**
     * Show progress dialog
     */
    public static void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Dismiss progress dialog
     */
    public static void dismissProgress() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    public static void shareWithTwitter(Activity activity, String title, String imagePath) {
        sActivity = activity;
        mTitle = title;

        submitBmpToTwitter(activity, imagePath);
    }

    private static AccessToken loadAccessToken(Activity activity) {
        SharedPreferences setting = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String access_token = setting.getString(TWITTER_ACCESS_TOKEN, null);
        String access_token_secret = setting.getString(TWITTER_ACCESS_TOKEN_SECRET, null);

        if (access_token == null || access_token_secret == null)
            return null;
        return new AccessToken(access_token, access_token_secret);
    }

    private static void saveAccessToken(Activity activity, AccessToken at) {
        Editor setting = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        setting.putString(TWITTER_ACCESS_TOKEN, at.getToken());
        setting.putString(TWITTER_ACCESS_TOKEN_SECRET, at.getTokenSecret());
        setting.commit();
    }

    private static boolean twitterLogin(Activity activity) {
        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);

        try {
            mRT = mTwitter.getOAuthRequestToken();
            Intent intent = new Intent(activity, TwitterLoginActivity.class);
            intent.putExtra("auth_url", mRT.getAuthorizationURL());
            activity.startActivityForResult(intent, TWITTER_LOGIN_CODE);
        } catch (Exception e) {
            if (ShareUtils.DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void submitBmpToTwitter(Activity activity, String imgPath) {
        AccessToken accessToken = loadAccessToken(activity);
        mImagePathForUploading = imgPath;
        if (accessToken == null)
            twitterLogin(activity);
        else
            uploadToTwitter(activity, mImagePathForUploading, accessToken);
    }

    public static void showInputPinDlg(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    getAccessToken(activity, "");
                }
            }).start();
        } else {
            getAccessToken(activity, "");
        }

//		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
//		alert.setTitle(null);
//		alert.setMessage("Please input Pin code.");
//		final Activity act = activity;
//		final EditText input = new EditText(activity);
//		input.setInputType(InputType.TYPE_CLASS_NUMBER);
//		alert.setView(input);
//		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int whichButton) {
//				String pinCode = input.getText().toString();
//				getAccessToken(act, pinCode);
//			}
//		});
//		alert.setNegativeButton("Cancel",
//			new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int whichButton) {
//					Toast.makeText(act, "Please input Pin-Code.", Toast.LENGTH_SHORT).show();
//				}
//			});
//
//		alert.show();
    }

    public static void getAccessToken(Activity activity, String pinCode) {
        if (mTwitter != null)
            try {
                AccessToken at = mTwitter.getOAuthAccessToken(ShareUtils.mRT, pinCode);
                saveAccessToken(activity, at);
                uploadToTwitter(activity, mImagePathForUploading, at);
            } catch (TwitterException e) {
                if (DEBUG) e.printStackTrace();
            }
    }

    public static void uploadToTwitter(Activity a, String imgPath, AccessToken at) {
        new MyTask(a, imgPath, at).execute();

//		String updateString = String.format("%s\n%s\n%s's 'promo' video.",
//				sActivity.getString(R.string.app_name),
//				sActivity.getString(R.string.app_url),
//				String.format(sActivity.getString(R.string.post_description), mName, mTitle));
//
//		try{
//	        StatusUpdate status = new StatusUpdate(updateString);
//	        status.setMedia(new File(imgPath));
//	        Status s = mTwitter.updateStatus(status);
//	        android.util.Log.e("RRRR", s.getText());
//	    } catch(TwitterException e){
//	        Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
//	    }
    }

    private static File copyFileFromAssets(String srcFile, String destFile) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = sActivity.getAssets().open(srcFile);
            File outFile = new File(destFile);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            return outFile;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + srcFile, e);
        }

        return null;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private static class LoginDialogListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            Toast.makeText(sActivity, "Facebook Login Success",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
            Toast.makeText(sActivity, error.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
            Toast.makeText(sActivity, error.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
            Toast.makeText(sActivity, "Facebook Login Cancel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static class ImageUploadListener extends BaseRequestListener {
        @Override
        public void onComplete(final String response, final Object state) {
            try {
                if (DEBUG) Log.d("ShareUtils:Facebook", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String src = json.getString("src");
                if (DEBUG) Log.d("ShareUtils:Facebook", "src: " + src);
                showSuccessText();
            } catch (Exception e) {
                showFailText();
            }
            dismissProgress();
        }
    }

    ;

    public static class SessionListener implements AuthListener, LogoutListener {
        @Override
        public void onAuthSucceed() {
            // Login Success!
            uploadBitmap();
        }

        @Override
        public void onAuthFail(String error) {
            // Login Fail
        }

        @Override
        public void onLogoutBegin() {
        }

        @Override
        public void onLogoutFinish() {
        }
    }

    static class MyTask extends AsyncTask<Void, String, String> {
        private ProgressDialog dialog;
        private Activity mActivity;
        private String mImgPath;
        private AccessToken mAt;

        public MyTask(Activity a, String str, AccessToken at) {
            super();
            mActivity = a;
            mImgPath = str;
            mAt = at;

            dialog = new ProgressDialog(mActivity);
            dialog.setTitle(null);
            dialog.setMessage("Please wait while uploading...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MyTask.this.cancel(true);
                    dialog.dismiss();
                }
            });
        }

        @Override
        protected void onPostExecute(String result) {
            String str = null;
            if (result != null) {
                str = "Success uploading photo to TwitPic " + result;
            } else {
                str = "Fail uploading photo to TwitPic";
            }
            Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Configuration conf = new ConfigurationBuilder()
                    .setMediaProviderAPIKey(TWITPIC_API_KEY)
                    .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                    .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
                    .setOAuthAccessToken(mAt.getToken())
                    .setOAuthAccessTokenSecret(mAt.getTokenSecret()).build();
            ImageUpload upload = new ImageUploadFactory(conf).getInstance(MediaProvider.TWITPIC);
            String url;

            String updateString = String.format("%s\n%s%s\n%s",
                    sActivity.getString(R.string.app_name),
                    sActivity.getString(R.string.app_url),
                    sActivity.getPackageName(), mTitle);

            try {
                url = upload.upload(new File(mImgPath), updateString);
                //url = upload.upload(copyFileFromAssets(mImgPath, "tmp"), updateString);
                return url;
            } catch (TwitterException e) {
                if (DEBUG) e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(mActivity, "Fail upload image to Twitpic",
                    Toast.LENGTH_SHORT).show();
            super.onCancelled();
        }
    }

}
