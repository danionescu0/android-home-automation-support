package homeautomation.dan.ionescu.homeautomation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {
    private static String URL = "https://bitbucket.org/danionescu/home-automation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(URL);
    }
}
