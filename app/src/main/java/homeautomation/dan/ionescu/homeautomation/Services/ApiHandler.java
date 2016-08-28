package homeautomation.dan.ionescu.homeautomation.Services;

import android.location.Location;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.concurrent.Callable;

public class ApiHandler {
    private UserPreferences userPreferences;
    private static final String TAG = "LocationService";

    public ApiHandler(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public void sendLocation(Location location, final Callable afterRequest) {
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams requestParams = buildRequestParams(location);

        client.get(getRequestUrl(), requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody) {
                Log.d(TAG, "api call success");
                try {
                    afterRequest.call();
                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] errorResponse, Throwable e) {
                Log.d(TAG, "api call failure");
                try {
                    afterRequest.call();
                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        });
    }

    private RequestParams buildRequestParams(Location location) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("username", userPreferences.getUsername());
        requestParams.put("password", userPreferences.getPassword());
        requestParams.put("latitude", Double.toString(location.getLatitude()));
        requestParams.put("longitude", Double.toString(location.getLongitude()));
        requestParams.put("device_name", userPreferences.getDeviceName());

        return requestParams;
    }

    private String getRequestUrl() {
        return String.format("%s/api/record-location", userPreferences.getServerURL());
    }
}
