package wenavi.jp.nal.loginlibrary.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright © Nals
 * Created by macintosh on 1/11/19.
 */

public final class ApiService {
    public static ApiService sInStance;
    private static final String URL_DOMAIN = "https://api.toyota-dev.wenavi.net/api/";
    public static final String URL_REGISTER_ENDPOINT = "registration";
    private static final int MAX_TIME_REQUEST = 15000;

    public static ApiService getsInStance() {
        if (sInStance == null) {
            sInStance = new ApiService();
        }
        return sInStance;
    }

    public HttpURLConnection getConnectionObj(String endPoint) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(URL_DOMAIN + endPoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(MAX_TIME_REQUEST);
            conn.setConnectTimeout(MAX_TIME_REQUEST);
            conn.setRequestProperty("Secret", "123123");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
