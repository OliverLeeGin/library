package wenavi.jp.nal.loginlibrary.api;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

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
            final Converter<ResponseBody, ApiError> converter = ApiService.getInstance().getRetrofit()
                    .responseBodyConverter(ApiError.class, ApiError.class.getAnnotations());

            ApiError apiErrorResponse = null;
            try {
                apiErrorResponse = converter.convert(response.errorBody());
            } catch (IOException e) {
                apiErrorResponse = new ApiError(response.code(), response.message());
                e.printStackTrace();
            }

            final int statusCode = apiErrorResponse.getStatusCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    String urlRequest = Uri.parse(call.request().url().toString()).getEncodedPath();
                    onHandleError401(urlRequest, apiErrorResponse);
                    return;
                case HttpURLConnection.HTTP_GONE:
                    onHandleError410(apiErrorResponse);
                    return;
                case 426:
                    onHandleError426(apiErrorResponse);
                    return;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    apiErrorResponse.setMessage("サーバの内部エラーにより、データが取得できません。");
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    apiErrorResponse.setMessage("サーバとの接続に失敗しました。");
                    break;
            }

            // Show the dialog error
            onFailure(apiErrorResponse);
        }

    }

    private void onHandleError401(String urlRequest, ApiError apiError) {
        onFailure(apiError);
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
    }

    boolean isActivityDestroyed() {
        try {
            final Activity activity = (Activity) mContext;
            return activity.isFinishing() || activity.isDestroyed();
        } catch (ClassCastException e) {
            return false;
        }
    }

    void onHandleError410(@NonNull ApiError apiError) {
    }

    void onHandleError426(@NonNull ApiError apiError) {
    }
}
