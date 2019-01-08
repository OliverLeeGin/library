package wenavi.jp.nal.loginlibrary.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 10/2/18.
 */
@Data
public class UserResponse {
    @SerializedName("data")
    private User user;
}
