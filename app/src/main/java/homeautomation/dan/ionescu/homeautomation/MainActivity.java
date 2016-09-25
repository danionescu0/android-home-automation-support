package homeautomation.dan.ionescu.homeautomation;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends BaseActivity {


    boolean alarmManagerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.loadUrl(userPrefferences.getServerURL());
        startAlarmManager();
    }

    private void startAlarmManager() {
        if (alarmManagerStarted) {
            return;
        }
        alarmManagerStarted = true;

        alarmRepeater.repeat(GpsTrackerAlarmReceiver.class, 60);
    }
}