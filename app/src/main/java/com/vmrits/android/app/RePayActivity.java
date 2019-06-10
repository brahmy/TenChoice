package com.vmrits.android.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RePayActivity extends AppCompatActivity {
    private WebView webView_repay;
    private final static String URL = "http://www.srimahalakshmihatcheries.com/api/user/payment.php?";
    private String string_mobile_number, string_amount, string_full_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay);

        Intent intent = getIntent();
        string_mobile_number = intent.getStringExtra("mobile_number");
        string_amount = intent.getStringExtra("amount");
        string_full_name = intent.getStringExtra("full_name");
        System.out.println("repayment_details:" + string_mobile_number + "," + string_amount + "," + string_full_name);

        initializeViews();
    }

    private void initializeViews() {
        webView_repay = findViewById(R.id.id_repay_webView);
        loadWebView();
    }

    private void loadWebView() {
        webView_repay.getSettings().setLoadsImagesAutomatically(true);
        webView_repay.getSettings().setJavaScriptEnabled(true);
        webView_repay.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView_repay.loadUrl(URL + "phone=" + string_mobile_number + "&amount=" + string_amount + "&full_name=" + string_full_name);
        webView_repay.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            } else {
                view.loadUrl(URL);
            }
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (webView_repay.canGoBack()) {
            webView_repay.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
