package homeautomation.dan.ionescu.homeautomation.Services;

import android.location.Location;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        requestParams.put("username", userPreferences.getUsername());
        doRequest(getLocationUrl(), "POST", requestParams);
    }

    public void sendVoiceCommand(String command) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("command", command);
        doRequest(getVoiceCommandUrl(), "POST", requestParams);
    }

    private String getTokenFromServer() {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("username", userPreferences.getUsername());
        requestParams.put("password", userPreferences.getPassword());
        String token = doRequest(getTokenAuthUrl(), "POST", requestParams);

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
        return "/api/location";
    }

    private String getVoiceCommandUrl() {
        return "/api/voice-command";
    }

    private String getTokenAuthUrl() {
        return "/api/user/token";
    }

    private String getBase64ApiTokenSecret() {
        return Base64.encodeToString(apiTokenSecret.getBytes(), Base64.DEFAULT);
    }

    private String execute(Request request) throws IOException {
        configureHttpClient();
        if (!request.url().toString().contains(getTokenAuthUrl())) {
            request = applyJwtToken(request);
        }
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException(String.format("Unexpected code: %s", response.toString()));
        }

        return response.body().string();
    }

    private String doRequest(String path, String method, HashMap<String, String> params) {
        final Request request = getRequestBuilder(path, method, params).build();

        try {
            return execute(request);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        return "";
    }

    private Request.Builder getRequestBuilder(String path, String method, HashMap<String, String> params) {
        final Request.Builder requestBuilder = new Request.Builder();
        String absoluteUrl = userPreferences.getServerURL() + path;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(absoluteUrl).newBuilder();

        switch (method) {
            case "GET":
                for (Map.Entry<String, String> param : params.entrySet()) {
                    urlBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
                String urlWithParams = urlBuilder.build().toString();

                return requestBuilder.url(urlWithParams).get();
            case "POST":
                FormBody.Builder requestBodhBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    requestBodhBuilder.add(param.getKey(), param.getValue());
                }
                RequestBody requestBody = requestBodhBuilder.build();

                return requestBuilder
                        .url(urlBuilder.build().toString())
                        .post(requestBody);
            default:
                throw new RuntimeException(String.format("Invalid method name %s", method));
        }
    }

    private Request applyJwtToken(Request request) throws IOException {
        if (!userPreferences.hasValidJwtToken(getBase64ApiTokenSecret())) {
            String token = getTokenFromServer();
            userPreferences.setJwtToken(token);
            Log.d(TAG, String.format("Adding new jwt token: %s", userPreferences.getJwtToken()));
        }

        request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", userPreferences.getJwtToken()))
                .build();

        return request;
    }

    private void configureHttpClient() {
//        client.setConnectTimeout(6, TimeUnit.SECONDS);
//        client.setReadTimeout(6, TimeUnit.SECONDS);
    }
}