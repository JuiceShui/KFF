package com.kf.juice.kff.App;

import android.app.Application;

import com.kf5.sdk.system.entity.KF5User;
import com.kf5.sdk.system.init.KF5SDKInitializer;

/**
 * Created by Juice on 2017/4/6.
 * To strive,to seek,to find,and not to give up;
 */

public class App  extends Application{
    private static KF5User kf5User;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        KF5SDKInitializer.init(getApplicationContext());
    }
    public static KF5User getKf5User()
    {
        if (kf5User!=null)
        {
            return kf5User;
        }
        else {
            kf5User=new KF5User();
            kf5User.setHelpAddress("yeeyun.kf5.com");
            kf5User.setAppid("00158e5b15b3ac273c3dfffcefb6cf076c5d7ebf42fa0ca5");
            return kf5User;
        }
    }
}
