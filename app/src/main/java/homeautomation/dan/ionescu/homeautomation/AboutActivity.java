package homeautomation.dan.ionescu.homeautomation;

import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends BaseActivity {
    private static String URL = "https://github.com/danionescu0/home-automation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(URL);
    }
}