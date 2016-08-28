package homeautomation.dan.ionescu.homeautomation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends BaseActivity {
//    static String APP_URL = "http://danionescu0.go.ro:8080";
    private static final String TAG = "MainActivity";
    boolean alarmManagerStarted = false;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        myWebView.loadUrl(userPrefferences.getServerURL());
        startAlarmManager();
    }

    private void startAlarmManager() {
        if (alarmManagerStarted) {
            return;
        }
        alarmManagerStarted = true;
        Context context = getBaseContext();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1 * 60000,
                pendingIntent);
    }
}
