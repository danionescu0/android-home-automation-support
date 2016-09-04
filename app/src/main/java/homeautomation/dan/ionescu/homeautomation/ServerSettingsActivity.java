package homeautomation.dan.ionescu.homeautomation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @Todo refactor repetitive setters and getters into a service
 */
public class ServerSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_settings);
        setSettingsValuesFromPreferences();
    }

    public void updateSettings(View view) {
        EditText serverURL = (EditText) findViewById(R.id.server_url);
        EditText apiPassword = (EditText) findViewById(R.id.api_password);
        EditText apiUsername = (EditText) findViewById(R.id.api_username);
        userPrefferences.setServerURL(serverURL.getText().toString());
        userPrefferences.setApiPasswword(apiPassword.getText().toString());
        userPrefferences.setApiUsername(apiUsername.getText().toString());

        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
    }

    private void setSettingsValuesFromPreferences() {
        EditText serverURL = (EditText) findViewById(R.id.server_url);
        EditText apiPassword = (EditText) findViewById(R.id.api_password);
        EditText apiUsername = (EditText) findViewById(R.id.api_username);
        serverURL.setText(userPrefferences.getServerURL());
        apiPassword.setText(userPrefferences.getPassword());
        apiUsername.setText(userPrefferences.getUsername());
    }
}