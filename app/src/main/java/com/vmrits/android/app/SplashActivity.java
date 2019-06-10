package com.vmrits.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

public class SplashActivity extends Activity {
    private LoginSessionManager loginSessionManager;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Hiding Title bar of this activity screen */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /** Making this activity, full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loginSessionManager = new LoginSessionManager(getApplicationContext());
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                checkIsLogin();
            }
        }, 5000);
    }

    private void checkIsLogin() {

        if (loginSessionManager.isLoggedIn()) {
            loginSessionManager.checkLogin();

            // get user data from session
            HashMap<String, String> user = loginSessionManager.getUserDetails();
            // number
            String mobile_number = user.get(LoginSessionManager.KEY_MOBILE_NUMBER);

            Intent intent = new Intent(SplashActivity.this, HomeLoanMainActivity.class);
            intent.putExtra("mobile_number", mobile_number);
            startActivity(intent);

        } else {
            Intent intent = new Intent(SplashActivity.this, MobileVerifyActivity.class);
            startActivity(intent);
        }
    }

}
