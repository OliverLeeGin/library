package wenavi.jp.nal.loginlibrary.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import wenavi.jp.nal.loginlibrary.model.error.Meta;

/**
 * Copyright © Nals
 * Created by macintosh on 1/11/19.
 */
@Data
public class UserResponse {
    private Meta meta;
    @SerializedName("data")
    private User user;
}
