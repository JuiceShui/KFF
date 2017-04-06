package com.kf.juice.kff;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kf.juice.kff.App.App;
import com.kf.juice.kff.utils.Preference;
import com.kf.juice.kff.utils.Utils;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.KF5User;
import com.kf5.sdk.system.entity.ParamsKey;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.widget.DialogBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_login)
    Button btn_login;
    private KF5User kf5User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        kf5User= App.getKf5User();
        etEmail.setText("123456@qq.com");
        etName.setText("chosen.kf5.com");
    }
    @OnClick(R.id.btn_login)
    void login()
    {
        final Map<String, String> map = new ArrayMap<>();
        map.put(ParamsKey.EMAIL, etEmail.getText().toString());
//                map.put(Field.PHONE, "18715965784");
        SPUtils.saveAppID(kf5User.getAppid());
        SPUtils.saveHelpAddress(kf5User.getHelpAddress());
        SPUtils.saveUserAgent(Utils.getAgent(new SoftReference<Context>(MainActivity.this)));
        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(final String result) {
                Log.i("kf5测试", "登录成功" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = SafeJson.parseObj(result);
                            int resultCode = SafeJson.safeInt(jsonObject, "error");
                            if (resultCode == 0) {
                                Preference.saveBoolLogin(MainActivity.this, true);
                                final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                if (userObj != null) {
                                    String userToken = userObj.getString(Field.USERTOKEN);
                                    int id = userObj.getInt(Field.ID);
                                    SPUtils.saveUserToken(userToken);
                                    SPUtils.saveUserId(id);
                                    new DialogBox(MainActivity.this)
                                            .setMessage("登录成功")
                                            .setLeftButton("取消", null)
                                            .setRightButton("确定", new DialogBox.onClickListener() {
                                                @Override
                                                public void onClick(DialogBox dialog) {
                                                    dialog.dismiss();
                                                    //MainActivity.this.finish();
                                                }
                                            }).show();
                                    saveToken(map);
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //                                            String message = jsonObject.getString("message");
                                        //                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        loginUser(map);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "登录失败" + result);
            }
        });
    }
    private void saveToken(Map<String, String> map) {
        map.put(ParamsKey.DEVICE_TOKEN, "123456");
        UserInfoAPI.getInstance().saveDeviceToken(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                Log.i("kf5测试", "保存设备Token成功" + result);
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "保存设备Token失败" + result);
            }
        });

        UserInfoAPI.getInstance().getUserInfo(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(String result) {

            }
        });
    }
    private void loginUser(final Map<String, String> map) {

        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {

            @Override
            public void onSuccess(final String result) {
                Log.i("kf5测试", "登录成功" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = SafeJson.parseObj(result);
                            int resultCode = SafeJson.safeInt(jsonObject, "error");
                            if (resultCode == 0) {
                                Preference.saveBoolLogin(MainActivity.this, true);
                                final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                if (userObj != null) {
                                    String userToken = userObj.getString(Field.USERTOKEN);
                                    int id = userObj.getInt(Field.ID);
                                    SPUtils.saveUserToken(userToken);
                                    SPUtils.saveUserId(id);
                                    new DialogBox(MainActivity.this)
                                            .setMessage("登录成功")
                                            .setLeftButton("取消", null)
                                            .setRightButton("确定", new DialogBox.onClickListener() {
                                                @Override
                                                public void onClick(DialogBox dialog) {
                                                    dialog.dismiss();
                                                    //MainActivity.this.finish();
                                                }
                                            }).show();
                                    saveToken(map);
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "登录失败" + result);
            }
        });
    }
}
