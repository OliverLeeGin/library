package wenavi.jp.nal.loginlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright © Nals
 * Created by TrangLT on 9/13/18.
 */
public class User implements Parcelable {
    private String email;
    private String birthday;
    private String username;

    public User() {

    }

    public User(String email, String birthday, String username) {
        this.email = email;
        this.birthday = birthday;
        this.username = username;
    }

    protected User(Parcel in) {
        email = in.readString();
        birthday = in.readString();
        username = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
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
    }
}
