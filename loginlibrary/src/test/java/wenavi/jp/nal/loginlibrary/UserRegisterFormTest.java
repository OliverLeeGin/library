package wenavi.jp.nal.loginlibrary;

import org.junit.Test;

import wenavi.jp.nal.loginlibrary.view.UserRegisterForm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Copyright © Nals
 * Created by macintosh on 1/11/19.
 */

public class UserRegisterFormTest {
    @Test
    public void updateUI() {
        // Trigger
        UserRegisterForm mUserRegisterForm =  mock(UserRegisterForm.class);;
        mUserRegisterForm.checkEnableRegister();
        verify(mUserRegisterForm);
//        Mockito.verify(mUserRegisterForm.mEdtEmail).setText("TrangLT");
        // Validation
    }
}
