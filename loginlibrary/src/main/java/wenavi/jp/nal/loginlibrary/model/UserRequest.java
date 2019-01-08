package wenavi.jp.nal.loginlibrary.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 10/2/18.
 */
@Data
public class UserRequest {
    private String email;
    private String password;
    private String birthday;
    private String username;
    @SerializedName("device_type")
    private int deviceType;
    @SerializedName("device_token")
    private String deviceToken;

}
