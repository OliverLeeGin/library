package wenavi.jp.nal.loginlibrary.api;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by TrangLT on 20/03/18.
 *
 * @param <T>
 */
public abstract class ApiResponse<T> implements Callback<T> {

    @NonNull
    private Context mContext;

    public ApiResponse(@NonNull Context context) {
        mContext = context;
    }

    public abstract void onResponse(T t, ApiError y);

    public void onFailure(ApiError apiError) {
        //  Log.d("TAG", "onFailure: " + apiError.getMessage());
        onResponse(null, apiError);
    }

    public void onStatusCodeSuccessful(int statusCode) {
        // Listen status code
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onStatusCodeSuccessful(response.code());
            onResponse(response.body(), new ApiError(response.code(), response.message()));
        } else {
            // Get the error
            if (response.code() == 422) {
                try {
                    if (response != null && response.errorBody() != null) {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (jsonObject.has("meta")) {
                            JSONObject metaObj = jsonObject.getJSONObject("meta");
                            if (metaObj.has("errors")) {
                                JSONObject errorObj = metaObj.getJSONObject("errors");
                                if (errorObj.has("email")) {
                                    onHandleError422(errorObj.getString("email"));
                                    return;
                                } else if (errorObj.has("old_password")) {
                                    onHandleError422(errorObj.getString("old_password"));
                                    return;
                                } else if (errorObj.has("postcode")) {
                                    onHandleError422(errorObj.getString("postcode"));
                                    return;
                                }
                            }
                        }
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            final Converter<ResponseBody, ApiErrorResponse> converter = ApiService.getInstance().getRetrofit()
                    .responseBodyConverter(ApiErrorResponse.class, ApiErrorResponse.class.getAnnotations());

            ApiErrorResponse apiErrorResponse = null;
            try {
                apiErrorResponse = converter.convert(response.errorBody());
            } catch (IOException e) {
                e.printStackTrace();
            }


            ApiError apiError;

            if (apiErrorResponse == null) {
                apiError = new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, "サーバの内部エラーにより、データが取得できません。");
            } else {
                apiError = new ApiError(apiErrorResponse.getMeta() != null ?
                        apiErrorResponse.getMeta().getStatus() : HttpURLConnection.HTTP_INTERNAL_ERROR,
                        apiErrorResponse.getMeta() != null ?
                                apiErrorResponse.getMeta().getMessage() : "サーバの内部エラーにより、データが取得できません。");
            }

            final int statusCode = apiError.getStatusCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    String urlRequest = Uri.parse(call.request().url().toString()).getEncodedPath();
                    onHandleError401(urlRequest, apiError);
                    return;
                case HttpURLConnection.HTTP_GONE:
                    onHandleError410(apiError);
                    return;
                case 426:
                    onHandleError426(apiError);
                    return;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    apiError.setMessage("サーバの内部エラーにより、データが取得できません。");
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    apiError.setMessage("サーバとの接続に失敗しました。");
                    break;
            }

            // Show the dialog error
            onFailure(apiError);
        }

    }

    private void onHandleError422(String email) {
        ApiError apiError = ApiError.builder()
                .statusCode(422)
                .message(email)
                .build();
        onFailure(apiError);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                .message(t.getLocalizedMessage())
                .build();
        onFailure(apiError);
        //  Crashlytics.logException(t);
    }

    boolean isActivityDestroyed() {
        try {
            final Activity activity = (Activity) mContext;
            return activity.isFinishing() || activity.isDestroyed();
        } catch (ClassCastException e) {
            return false;
        }
    }

    @CallSuper
    void onHandleError401(@NonNull String urlRequest, @NonNull ApiError apiError) {
        // Reset access token
        onFailure(apiError);
    }

    void onHandleError410(@NonNull ApiError apiError) {
    }

    void onHandleError426(@NonNull ApiError apiError) {
    }
}
