package com.vmrits.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class PaymentActivity extends AppCompatActivity {
    private TextView textView_amount, textView_approved_date, textView_current_date, textView_duriation;
    private Button button_repay;
    private String string_amount, string_mobile_number, intent_string_mobile_no, string_approved_date, string_currrent_Date, string_duriation, string_full_name;
    private Context context = PaymentActivity.this;
    private LoginSessionManager loginSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        loginSessionManager = new LoginSessionManager(context);
        HashMap<String, String> mNumber = loginSessionManager.getUserDetails();
        string_mobile_number = mNumber.get(LoginSessionManager.KEY_MOBILE_NUMBER);


        Intent intent = getIntent();
        intent_string_mobile_no = intent.getStringExtra("mobile_number");

        if (string_mobile_number != null || intent_string_mobile_no != null) {

            volleyGetRepaymentDetails();
        }


//        volleyRepayStatus();
        initializeViews();
    }

    private void volleyRepayStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.TRANSACTION_PAYMENT_STATUS + "?phone=" + intent_string_mobile_no, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("payment_status" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("pending")) {
                        volleyGetRepaymentDetails();
                    } else {
                        Toast.makeText(context, "Your Repayment Done Successfully!Thank You", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sorry!Server error!!", Toast.LENGTH_LONG).show();

            }
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("phone",intent_string_mobile_no);
                return params;
            }
        }*/;
        AppController.getInstance().addToRequestQueue(stringRequest, "payment_statuss");

    }

    private void volleyGetRepaymentDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.REPAYMENT_DETAILS + "?phone=" + string_mobile_number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("payment_detailss" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String loan_status = jsonObject.getString("status");

                    if (loan_status.contains("Details Not Found")) {
                        loadPendingDialog();
                    } else {
                        string_amount = jsonObject.getString("amount");
                        string_approved_date = jsonObject.getString("approve_date");
                        string_currrent_Date = jsonObject.getString("repayment_date");
                        string_duriation = jsonObject.getString("Duration");
                        string_full_name = jsonObject.getString("full_name");

//₹
                        textView_amount.setText("Rs " + string_amount);
                        textView_approved_date.setText(string_approved_date);
                        textView_current_date.setText(string_currrent_Date);
                        textView_duriation.setText(string_duriation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        AppController.getInstance().addToRequestQueue(stringRequest, "payment_detailss");
    }

    private void initializeViews() {
        textView_amount = findViewById(R.id.id_amount_payment_text_view);
        textView_approved_date = findViewById(R.id.id_payment_approved_time_txtVw);
        textView_current_date = findViewById(R.id.id_payment_current_time_txtVw);
        textView_duriation = findViewById(R.id.id_amount_payment_duriation_text_view);
        button_repay = findViewById(R.id.id_amount_payment_repay_button);

        onClickViews();
    }

    private void onClickViews() {
        button_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RePayActivity.class);
                if (intent_string_mobile_no != null) {
                    intent.putExtra("mobile_number", intent_string_mobile_no);
                } else {
                    intent.putExtra("mobile_number", string_mobile_number);
                }
                intent.putExtra("amount", string_amount);
                intent.putExtra("full_name", string_full_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void loadPendingDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pending);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
    }

}
