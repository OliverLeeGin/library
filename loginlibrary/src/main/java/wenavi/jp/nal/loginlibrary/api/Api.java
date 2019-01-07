package wenavi.jp.nal.loginlibrary.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import wenavi.jp.nal.loginlibrary.model.User;

/**
 * Copyright © Nals
 * Created by TrangLT on 21/03/18.
 */

public interface Api {
    @POST("users")
    Call<User> registerUser(@Body User user);

}
