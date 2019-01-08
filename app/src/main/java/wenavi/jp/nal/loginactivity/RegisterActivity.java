package wenavi.jp.nal.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import wenavi.jp.nal.loginlibrary.model.User;
import wenavi.jp.nal.loginlibrary.view.UserRegisterForm;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */

public class RegisterActivity extends AppCompatActivity implements UserRegisterForm.IOnResultListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        UserRegisterForm userRegisterForm = findViewById(R.id.tv_test);
        userRegisterForm.setRegisterResponseListener(this);
    }

    @Override
    public void success(User user) {
        Intent intent = new Intent();
        intent.putExtra("user", user.getUserName());
        setResult(RESULT_OK, intent);
        finish();
    }

}
