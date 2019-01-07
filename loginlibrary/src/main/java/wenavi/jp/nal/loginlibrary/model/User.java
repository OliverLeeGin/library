package wenavi.jp.nal.loginlibrary.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */
@Data
@AllArgsConstructor
public class User {
    @SerializedName("name")
    private String userName;
    private String dob;
    private String email;
    private String password;
}
