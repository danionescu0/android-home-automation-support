package homeautomation.dan.ionescu.homeautomation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import homeautomation.dan.ionescu.homeautomation.Services.UserPreferences;

public abstract class BaseActivity extends AppCompatActivity {
    protected UserPreferences userPrefferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefferences = new UserPreferences(getApplicationContext());
        setContentView(R.layout.activity_base_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        }

        return true;
    }

    private void showActivity(Class<?>  activityClass) {
        Intent showActivity = new Intent(this, activityClass);
        this.startActivity(showActivity);
    }
}
