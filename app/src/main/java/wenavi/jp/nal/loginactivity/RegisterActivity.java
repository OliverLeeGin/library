package wenavi.jp.nal.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import wenavi.jp.nal.loginlibrary.model.User;
import wenavi.jp.nal.loginlibrary.view.UserRegisterForm;

/**
 * Copyright © Nals
 * Created by macintosh on 1/8/19.
 */

public class RegisterActivity extends AppCompatActivity implements UserRegisterForm.IOnResultListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        UserRegisterForm userRegisterForm = findViewById(R.id.tv_test);
        userRegisterForm.setRegisterResponseListener(this);
        UserManger user = new UserManger();
    }

    @Override
    public void success(User user) {
        setValues(user.getEmail());
    }

    @Override
    public void onFailed(String errorStr) {
        setValues(errorStr);
    }

    private void setValues(String value) {
        Intent intent = new Intent();
        intent.putExtra("value", value);
        setResult(RESULT_OK, intent);
        finish();
    }
}
