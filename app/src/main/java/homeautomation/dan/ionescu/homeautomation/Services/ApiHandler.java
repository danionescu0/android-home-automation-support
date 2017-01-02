package homeautomation.dan.ionescu.homeautomation.Services;

import android.location.Location;
import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;

public class ApiHandler {
    private UserPreferences userPreferences;
    private String apiTokenSecret;
    private static final String TAG = "LocationService";

    protected OkHttpClient client = new OkHttpClient();

    @Inject
    public ApiHandler(UserPreferences userPreferences, String apiTokenSecret) {
        this.userPreferences = userPreferences;
        this.apiTokenSecret = apiTokenSecret;
    }

    public void sendLocation(Location location) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("latitude", Double.toString(location.getLatitude()));
        requestParams.put("longitude", Double.toString(location.getLongitude()));
        try {
            doRequest(getLocationUrl(), "GET", requestParams);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void sendVoiceCommand(String command) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("command", command);
        try {
            doRequest(getVoiceCommandUrl(), "GET", requestParams);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private String getTokenFromServer() {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("username", userPreferences.getUsername());
        requestParams.put("password", userPreferences.getPassword());
        String token = "";
        try {
            token = doRequest(getTokenAuthUrl(), "GET", requestParams);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, "response(" + new String(token)+")");
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(getBase64ApiTokenSecret())
                    .parseClaimsJws(token);
            Log.d(TAG, claims.toString());
        } catch (MissingClaimException | IncorrectClaimException | SignatureException
                |IllegalArgumentException | ExpiredJwtException e) {
            Log.d(TAG, e.getMessage());
        }

        return token;
    }

    private String getLocationUrl() {
        return "/api/record-location";
    }

    private String getVoiceCommandUrl() {
        return "/api/voice-command";
    }

    private String getTokenAuthUrl() {
        return "/api/token-auth";
    }

    private String getBase64ApiTokenSecret() {
        return Base64.encodeToString(apiTokenSecret.getBytes(), Base64.DEFAULT);
    }

    private String execute(Request request) throws IOException {
        configureHttpClient();
        if (!request.httpUrl().url().toString().contains(getTokenAuthUrl())) {
            request = applyJwtToken(request);
        }
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException(String.format("Unexpected code: %s", response.toString()));
        }

        return response.body().string();
    }

    private String doRequest(String path, String method, HashMap<String, String> params) throws IOException{
        final Request request = getRequestBuilder(path, method, params).build();

        return execute(request);
    }

    private Request.Builder getRequestBuilder(String path, String method, HashMap<String, String> params) {
        final Request.Builder requestBuilder = new Request.Builder();
        String absoluteUrl = userPreferences.getServerURL() + path;

        switch (method) {
            case "GET":
                HttpUrl.Builder urlBuilder = HttpUrl.parse(absoluteUrl).newBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    urlBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
                String urlWithParams = urlBuilder.build().toString();

                return requestBuilder.url(urlWithParams).get();
            default:
                throw new RuntimeException(String.format("Invalid method name %s", method));
        }
    }

    private Request applyJwtToken(Request request) throws IOException {
        if (!userPreferences.hasValidJwtToken(getBase64ApiTokenSecret())) {
            String token = getTokenFromServer();
            userPreferences.setJwtToken(token);
        }

        Log.d(TAG, String.format("adding jwt token: %s", userPreferences.getJwtToken()));
        request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", userPreferences.getJwtToken()))
                .build();

        return request;
    }

    private void configureHttpClient() {
        client.setConnectTimeout(6, TimeUnit.SECONDS);
        client.setReadTimeout(6, TimeUnit.SECONDS);
    }
}