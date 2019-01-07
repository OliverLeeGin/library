package wenavi.jp.nal.loginlibrary.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */
@Data
public class Food {
    @SerializedName("name")
    private String nameFood;
}
