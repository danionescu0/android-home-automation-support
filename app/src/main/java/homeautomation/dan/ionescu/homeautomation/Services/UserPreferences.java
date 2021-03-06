package homeautomation.dan.ionescu.homeautomation.Services;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Strings;

public class UserPreferences {
    static private String FILE_NAME = "home_automation";

    private String SERVER_URL = "server_url";
    private String API_USERNAME = "api_username";
    private String API_PASSWORD = "api_password";
    private String JWT_TOKEN = "jwt_token";
    private String WEBAPP_URL = "%s/index.html";

    private Context context;

    @Inject
    public UserPreferences(Context context) {
        this.context = context;
    }

    public String getServerURL() {
        return get(SERVER_URL);
    }

    public String getWebappURL() {
        return String.format(this.WEBAPP_URL, get(SERVER_URL));
    }

    public String getUsername() {
        return get(API_USERNAME);
    }

    public String getPassword() {
        return get(API_PASSWORD);
    }

    public String getJwtToken() {
        return get(JWT_TOKEN);
    }

    public void setServerURL(String name) {
        set(SERVER_URL, name);
    }

    public void setApiUsername(String username) {
        set(API_USERNAME, username);
    }

    public void setApiPasswword(String passwword) {
        set(API_PASSWORD, passwword);
    }

    public void setJwtToken(String token) {
        set(JWT_TOKEN, token);
    }

    public boolean hasValidJwtToken(String tokenSecret) {
        if (getJwtToken().length() == 0) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(getJwtToken());
        } catch (ExpiredJwtException | SignatureException e) {
            return false;
        }

        return true;
    }

    public boolean hasServerURL() {
        return getServerURL() != "";
    }

    private String get(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        return sharedPref.getString(key, "");
    }

    private void set(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}