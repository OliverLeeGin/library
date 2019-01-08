package wenavi.jp.nal.loginlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 9/13/18.
 */
@Data
public class User implements Parcelable {
    private String email;
    private String birthday;
    private String username;
    @SerializedName("role_id")
    private int roleId;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("id")
    private int id;
    private String address1;
    private String address2;
    @SerializedName("postcode")
    private String postCode;
    @SerializedName("is_receive_notification_emergency")
    private int turnOnEmergence;

    public User() {
        //no-op
    }

    protected User(Parcel in) {
        email = in.readString();
        birthday = in.readString();
        username = in.readString();
        roleId = in.readInt();
        updatedAt = in.readString();
        createdAt = in.readString();
        id = in.readInt();
        turnOnEmergence = in.readInt();
        postCode = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(username);
        dest.writeInt(roleId);
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeInt(id);
        dest.writeString(postCode);
        dest.writeInt(turnOnEmergence);
    }

    public String getAddress() {
        return getAddress1() + " " + getAddress2();
    }

    public String getAddress1() {
        return !TextUtils.isEmpty(address1) ? address1 : "";
    }

    public String getAddress2() {
        return !TextUtils.isEmpty(address2) ? address2 : "";
    }
}
