package com.android.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.project.material.R;
import com.project.material.util.ShareUtils;

public class TwitterLoginActivity extends Activity {
    Intent mIntent;

    /*
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.twitter_login_webview);
            WebView webView = (WebView) findViewById(R.id.WebView);

            String url = mIntent.getStringExtra("auth_url");
            webView.loadUrl(url);
        }

        class JavaScriptInterface {
            public void getPinCode(String pin) {
                if (pin.length() > 0) {
                    mIntent.putExtra("pin_code", pin);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    Log.v("DBG", "get pin failed...");
                }
            }
        }
     */
    WebView myWebView;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twitter_login_webview);
        myWebView = (WebView) findViewById(R.id.WebView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);

        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.addJavascriptInterface(new MyJavaScriptClient(), "HTMLOUT");

        mIntent = getIntent();
        String url = mIntent.getStringExtra("auth_url");
        myWebView.loadUrl(url);
    }

    /**
     * Class that injects the JavaScript callback when the 2nd auth
     * page was reached.
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.equals("https://api.twitter.com/oauth/authorize")) {
                myWebView.loadUrl("javascript:window.HTMLOUT.obtain(document.body.innerHTML);");
                // not sure why the following fails, but doesn't really matter
                // myWebView.loadUrl("javascript:window.HTMLOUT.setHTML(document.getElementById('code-desc'));");
            }
        }
    }

    /**
     * This class holds the callback that we inject in MyWebViewClient#onPageFinished
     */
    public class MyJavaScriptClient {

        /**
         * Called from the java script in the web view. We parse the
         * html obtained and then generate an account with the help of the
         * passed pin
         *
         * @param html Html as evaluated by the javascript
         */
        public void obtain(String html) {
            int i = html.indexOf("<code>");
            if (i != -1) {
                html = html.substring(i + 6);
                i = html.indexOf("</code>");
                String pin = html.substring(0, i);

                try {
                    mIntent.putExtra(ShareUtils.PIN_CODE_EXTRA, pin);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(TwitterLoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

}