package homeautomation.dan.ionescu.homeautomation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import homeautomation.dan.ionescu.homeautomation.Services.ApiHandler;
import homeautomation.dan.ionescu.homeautomation.Services.MetaDataContainer;

public class MainActivity extends BaseActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
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

    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void startAlarmManager() {
        if (alarmManagerStarted) {
            return;
        }
        alarmManagerStarted = true;
        alarmRepeater.repeat(GpsTrackerAlarmReceiver.class, 60);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            final String spokenText = results.get(0);
            Log.d("cici", spokenText);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    apiHandler.sendVoiceCommand(spokenText);
                    return null;
                }
            }.execute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}