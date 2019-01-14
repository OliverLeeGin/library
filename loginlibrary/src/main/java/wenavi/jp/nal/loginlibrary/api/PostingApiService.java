package wenavi.jp.nal.loginlibrary.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Copyright © Nals
 * Created by macintosh on 1/11/19.
 */

public final class PostingApiService extends AsyncTask<String, Void, String> {
    private String mEndPoint;
    private JSONObject mJsonObject;
    private IOnResponseResult mIOnResponseResult;

    public PostingApiService(String endPoint, JSONObject jsonObject, IOnResponseResult iOnResponseResult) {
        mEndPoint = endPoint;
        mJsonObject = jsonObject;
        mIOnResponseResult = iOnResponseResult;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        HttpURLConnection conn = null;
        try {
            conn = ApiService.getsInStance().getConnectionObj(mEndPoint);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            OutputStream os = null;

            os = conn.getOutputStream();
            BufferedWriter writer = null;
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(mJsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
        } catch (Exception e) {
            Log.d("llt", "errorSendRequest: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            assert conn != null;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                return sb.toString();
            } else {
                BufferedReader inError = new BufferedReader(new
                        InputStreamReader(
                        conn.getErrorStream()));

                StringBuilder sbError = new StringBuilder("");
                String lineError = "";
                while ((lineError = inError.readLine()) != null) {
                    sbError.append(lineError);
                }
                inError.close();
                return sbError.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mIOnResponseResult != null) {
            mIOnResponseResult.onResponse(result);
        }
    }

    public interface IOnResponseResult {
        void onResponse(String result);
    }
}
