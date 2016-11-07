package homeautomation.dan.ionescu.homeautomation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import javax.inject.Inject;

import homeautomation.dan.ionescu.homeautomation.Services.AlarmRepeater;
import homeautomation.dan.ionescu.homeautomation.Services.ApiHandler;
import homeautomation.dan.ionescu.homeautomation.Services.UserPreferences;

public abstract class BaseActivity extends AppCompatActivity {

    HomeAutomationApplication app;

    @Inject UserPreferences userPrefferences;
    @Inject AlarmRepeater alarmRepeater;
    @Inject ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (HomeAutomationApplication) getApplication();
        app.getAppComponent().inject(this);
        setContentView(R.layout.activity_base_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.server_settings:
                showActivity(ServerSettingsActivity.class);
                break;
            case R.id.controll_interface:
                showActivity(MainActivity.class);
                break;
            case R.id.about:
                showActivity(AboutActivity.class);
                break;
        }

        return true;
    }

    private void showActivity(Class<?>  activityClass) {
        Intent showActivity = new Intent(this, activityClass);
        this.startActivity(showActivity);
    }
}