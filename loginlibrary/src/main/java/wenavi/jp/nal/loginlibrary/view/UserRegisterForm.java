package wenavi.jp.nal.loginlibrary.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import wenavi.jp.nal.loginlibrary.R;
import wenavi.jp.nal.loginlibrary.config.TimerUtils;
import wenavi.jp.nal.loginlibrary.model.User;
import wenavi.jp.nal.loginlibrary.networks.NetworkUtils;

/**
 * Copyright © Nals
 * Created by macintosh on 1/7/19.
 */

public class UserRegisterForm extends RelativeLayout implements DatePickerDialog.OnDateSetListener,
        View.OnTouchListener, View.OnClickListener {
    private final float ALPHA_ENABLE = 1f;
    private final float ALPHA_DISABLE = 0.5f;
    private final String URL_DOMAIN = "https://api.toyota-dev.wenavi.net/api/";
    private final String URL_REGISTER_ENDPOINT = "registration";
    private final int MAX_TIME_REQUEST = 15000;

    private LinearLayout mLlRegisterContaier;
    private EditText mEdtUserName;
    private TextView mTvBirthday;
    private EditText mEdtEmail;
    private EditText mEdtPassWord;
    private Context mContext;
    private TextView mBtnRegister;
    private View mViewOverlay;
    private DatePickerDialog mDatePickerDialog;
    private InputMethodManager mImm;
    private Rect mRect;
    private IOnResultListener mIOnResultListener;

    public UserRegisterForm(Context context) {
        super(context);
        mContext = context;
    }

    public UserRegisterForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.register_form, this, true);
        mEdtUserName = findViewById(R.id.edt_user_name);
        mTvBirthday = findViewById(R.id.edt_birth_day);
        mEdtEmail = findViewById(R.id.edt_contact);
        mEdtPassWord = findViewById(R.id.edt_password);
        mBtnRegister = findViewById(R.id.btn_register);
        mViewOverlay = findViewById(R.id.view_overlay);
        mLlRegisterContaier = findViewById(R.id.ll_container_register_root_view);


        mEdtUserName.addTextChangedListener(mGeneralTextWatcher);
        mEdtEmail.addTextChangedListener(mGeneralTextWatcher);
        mEdtPassWord.addTextChangedListener(mGeneralTextWatcher);
        mBtnRegister.setOnTouchListener(this);
        mTvBirthday.setOnClickListener(this);
        mBtnRegister.setEnabled(false);
        mBtnRegister.setAlpha(mBtnRegister.isEnabled() ? ALPHA_ENABLE : ALPHA_DISABLE);
        mLlRegisterContaier.setOnClickListener(this);
        mImm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
    }

    private TextWatcher mGeneralTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkEnableRegister();
        }
    };

    private void checkEnableRegister() {
        mBtnRegister.setEnabled(!TextUtils.isEmpty(mEdtUserName.getText()) &&
                !TextUtils.isEmpty(mTvBirthday.getText()) &&
                !TextUtils.isEmpty(mEdtEmail.getText()) &&
                !TextUtils.isEmpty(mEdtPassWord.getText()));
        mBtnRegister.setAlpha(mBtnRegister.isEnabled() ?
                ALPHA_ENABLE : ALPHA_DISABLE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.JAPANESE);
        calendar.set(year, month, dayOfMonth);
        if (calendar.after(Calendar.getInstance(Locale.JAPANESE))) {
            return;
        }
        mTvBirthday.setText(TimerUtils.getDatetime(calendar.getTimeInMillis(),
                TimerUtils.FormatType.TYPE_14));
        checkEnableRegister();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                mViewOverlay.setVisibility(View.VISIBLE);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    mViewOverlay.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    mViewOverlay.setVisibility(View.GONE);
                    if (!NetworkUtils.isNetworkConnected(mContext)) {
                        Toast.makeText(mContext, "Plz check your network connection", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    register();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                mViewOverlay.setVisibility(View.GONE);
                return true;
        }
        return false;
    }

    public void setRegisterResponseListener(IOnResultListener iOnResultListener) {
        mIOnResultListener = iOnResultListener;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.edt_birth_day) {
            onClickBirthday();
        }
        if (i == R.id.ll_container_register_root_view) {
            hideKeyboard();
        }
    }

    private void onClickBirthday() {
        Calendar calendar = Calendar.getInstance(Locale.JAPANESE);
        calendar = Calendar.getInstance(Locale.JAPANESE);
        calendar.set(1980, Calendar.JANUARY, 1);
        if (mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialog(mContext, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance(Locale.JAPANESE).getTimeInMillis());
        mDatePickerDialog.show();
    }

    private void hideKeyboard() {
        mImm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    private void register() {
        new RegisterUser().execute();
    }

    public interface IOnResultListener {
        void success(User user);

        void onFailed(String message);
    }

    private final class RegisterUser extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {

            URL url = null;
            HttpURLConnection
                    conn = null;
            try {
                url = new URL(URL_DOMAIN + URL_REGISTER_ENDPOINT);
                JSONObject postDataParams = createUserResuest();
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(MAX_TIME_REQUEST);
                conn.setConnectTimeout(MAX_TIME_REQUEST);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Secret", "123123");
                conn.setRequestProperty("content-type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = null;

                os = conn.getOutputStream();
                BufferedWriter writer = null;
                writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());
                writer.flush();
                writer.close();
                os.close();
            } catch (Exception e) {
                Log.d("llt", "errorSendRequest: " + e.getMessage());
                e.printStackTrace();
            }
            try {
                assert conn != null;
                if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();
                    return sb.toString();
                } else {
                    BufferedReader inError = new BufferedReader(new
                            InputStreamReader(
                            conn.getErrorStream()));

                    StringBuilder sbError = new StringBuilder("");
                    String lineError = "";
                    while ((lineError = inError.readLine()) != null) {
                        sbError.append(lineError);
                    }
                    inError.close();
                    return sbError.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.has("meta")) {
                        if (jsonObject.has("meta")) {
                            JSONObject metaObj = jsonObject.getJSONObject("meta");
                            if (metaObj.has("errors")) {
                                JSONObject errorObj = metaObj.getJSONObject("errors");
                                if (errorObj.has("email")) {
                                    if (mIOnResultListener != null) {
                                        mIOnResultListener.onFailed(errorObj.getString("email"));
                                    }
                                }
                            }
                        }
                    }

                    if (jsonObject.has("data")) {
                        JSONObject userObj = jsonObject.getJSONObject("data");
                        Log.d("llt", "onPostExecute: " + userObj);
                        User user = new User();
                        if (userObj.has("email")) {
                            user.setEmail(userObj.getString("email"));
                        }
                        if (mIOnResultListener != null) {
                            mIOnResultListener.success(user);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject createUserResuest() throws JSONException {
        JSONObject postDataParams = new JSONObject();
        postDataParams.put("email", mEdtEmail.getText().toString());
        postDataParams.put("password", mEdtPassWord.getText().toString());
        postDataParams.put("birthday", TimerUtils.getDatetime(TimerUtils
                        .convertStringToCalendarType1(mTvBirthday.getText().toString()).getTimeInMillis(),
                TimerUtils.FormatType.TYPE_1));
        postDataParams.put("username", mEdtUserName.getText().toString());
        postDataParams.put("device_type", 1);
        postDataParams.put("device_token", "eL6XwQe_gPE:APA91bFUCS-y1lyIpp_KHe");
        Log.e("params", postDataParams.toString());
        return postDataParams;

    }
}
