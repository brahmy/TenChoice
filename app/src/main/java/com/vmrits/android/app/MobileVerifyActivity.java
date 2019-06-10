package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MobileVerifyActivity extends AppCompatActivity {
    private EditText editText_mobile_number, editText_very_otp;
    private Button button_submit, button_otp_verify;
    private LinearLayout linearLayout_mobile_number, linearLayout_verify_otp;
    private String string_mobile_number, string_otp, OTP, string_Random_otp;

    private Context context = MobileVerifyActivity.this;
    private boolean isExist;

    private LoginSessionManager loginSessionManager;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);
        loginSessionManager = new LoginSessionManager(context);
        dialogProgressBar = new DialogProgressBar(context);
        dialogProgressBar.dialogInit();

        initializeViews();
    }


    private void initializeViews() {
        editText_mobile_number = findViewById(R.id.id_mobile_verify_mobile_number_edt);
        editText_very_otp = findViewById(R.id.id_mobile_verify_OTP_edt);
        button_submit = findViewById(R.id.id_mobile_verify_submit_btn);
        button_otp_verify = findViewById(R.id.id_mobile_verify_confirm_btn);
        linearLayout_mobile_number = findViewById(R.id.id_mobile_verify_LL);
        linearLayout_verify_otp = findViewById(R.id.id_mobile_verify_otp_LL);

        linearLayout_verify_otp.setVisibility(View.GONE);
        button_otp_verify.setVisibility(View.GONE);

        onClickViews();
    }

    private void onClickViews() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_mobile_number = editText_mobile_number.getText().toString();
                if (!TextUtils.isEmpty(string_mobile_number) && string_mobile_number.length() == 10) {
                    dialogProgressBar.showDialog();
                    volleyVerifyMobileRequest();

                } else {
                    editText_mobile_number.setError("Mobile is Empty/Incorrect!!");
                }
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_mobile_number.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText_mobile_number.addTextChangedListener(textWatcher);

        button_otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_otp = editText_very_otp.getText().toString();
                System.out.println("otpvalod" + OTP + "" + string_otp);
                if (OTP.contains(string_otp) && !TextUtils.isEmpty(OTP)) {
                    if (isExist) {
                        dialogProgressBar.showDialog();
                        Intent intent = new Intent(MobileVerifyActivity.this, HomeLoanMainActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);
//                        volleyPaymentStatus();

                    } else {
                        dialogProgressBar.showDialog();
                        Intent intent = new Intent(MobileVerifyActivity.this, SignUpActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);
                    }
                } else {
                    editText_very_otp.setError("OTP is Not Valid!");
                }

            }
        });
        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText_very_otp.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText_very_otp.addTextChangedListener(textWatcher1);
    }

    private void volleyPaymentStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.PAYMENT_STATUS + "?phone=" + string_mobile_number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("payment_status:" + response);
//                Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();
                dialogProgressBar.hideDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String string_Status = jsonObject.getString("status");

                    if (string_Status.equalsIgnoreCase("Details Not Found")) {
                        Intent intent = new Intent(MobileVerifyActivity.this, HomeLoanMainActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    } else if (string_Status.equalsIgnoreCase("pending")) {
                        Intent intent = new Intent(MobileVerifyActivity.this, HomeLoanMainActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    } else if (string_Status.equalsIgnoreCase("approved")) {
                        //HomeLoanMainActivity
                        Intent intent = new Intent(MobileVerifyActivity.this, PaymentActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    } else if (string_Status.equalsIgnoreCase("documents Not Submitted")) {
                        Intent intent = new Intent(MobileVerifyActivity.this, KYCActivity.class);
                        startActivity(intent);

//                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                Toast.makeText(context, "Sorry!Server error!!", Toast.LENGTH_LONG).show();
            }
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("phone","8888888888");
                return params;
            }
        }*/;
        AppController.getInstance().addToRequestQueue(stringRequest, "Payment_status");
    }

    private void volleyVerifyMobileRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.VERIFY_NUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                generateOTP();
                OTP = randomOTP();
                dialogProgressBar.hideDialog();
                System.out.println("verify_number_response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_response = jsonObject.getString("message");
                    if (string_response.equalsIgnoreCase("not exist")) {
                        isExist = false;

                        linearLayout_verify_otp.setVisibility(View.VISIBLE);
                        button_otp_verify.setVisibility(View.VISIBLE);

                        linearLayout_mobile_number.setVisibility(View.GONE);
                        button_submit.setVisibility(View.GONE);

                    } else {
                        loginSessionManager.createLoginSession(string_mobile_number);
                        isExist = true;
                        linearLayout_verify_otp.setVisibility(View.VISIBLE);
                        button_otp_verify.setVisibility(View.VISIBLE);

                        linearLayout_mobile_number.setVisibility(View.GONE);
                        button_submit.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                System.out.println("verify_number_error:" + error.getMessage());
                Toast.makeText(context, "Sorry!Server error!!", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", string_mobile_number);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "verify_number");

    }

    private void generateOTP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.GENERATE_OTP_, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("OTP:" + response);
                //OK:201271628
                Toast.makeText(context, "Your OTP is sent!Please verify!!", Toast.LENGTH_LONG).show();
//                OTP=response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sorry!Server error!!", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //User=crest&passwd=crest@123&mtype=N&Language=English&sid=crest&mobilenumber=9493302738&Message=Your+One+Time+Password+is%3A46351056
                HashMap<String, String> params = new HashMap<>();
                params.put("User", "crest");
                params.put("passwd", "crest@123");
                params.put("mtype", "N");
                params.put("Language", "English");
                params.put("sid", "crest");
                params.put("mobilenumber", string_mobile_number);
                params.put("Message", "Your OTP Here " + OTP + " .Please Don't Share with other");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "generate_otp");
    }

    private String randomOTP() {
        Random random = new Random();
        String OTP = String.format("%04d", random.nextInt(10000));
        return OTP;
    }
}
