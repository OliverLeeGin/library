package wenavi.jp.nal.loginlibrary.model.error;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 9/27/18.
 */
@Data
public class Meta  {
    private String message;
    private int status;
    private String token;
    @SerializedName("errors")
    private Error error;
}
