package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class AmountDetailsActivity extends AppCompatActivity {
    private Button button_get_money;
    private LinearLayout linearLayout_amount_bg;
    private TextView textView_amount, textView_emi, textView_days, textView_disbursal_amount, textView_active_amount,
            textView_processing_fee, textView_gst, textView_total_interest, textView_annual_interest;
    private String string_amount, intent_mobile_number, string_days, string_emi, string_mobile_number, string_disbursal_amount, string_active_amount, string_processing_fee, string_gst, string_total_interest, string_annual_interest;
    private Context context = AmountDetailsActivity.this;
    private LoginSessionManager loginSessionManager;
    private DialogProgressBar dialogProgressBar;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_details);
        loginSessionManager = new LoginSessionManager(context);
        dialogProgressBar = new DialogProgressBar(context);
        dialogProgressBar.dialogInit();


        Intent intent = getIntent();
        string_amount = intent.getStringExtra("amount");
        string_days = intent.getStringExtra("days");
        string_emi = intent.getStringExtra("emi");
        intent_mobile_number = intent.getStringExtra("mobile_number");

        if (loginSessionManager.isLoggedIn()) {
            HashMap<String, String> hashMap = loginSessionManager.getUserDetails();
            string_mobile_number = hashMap.get(LoginSessionManager.KEY_MOBILE_NUMBER);
        } else {
            Intent intent_login = new Intent(context, SignUpActivity.class);
            startActivity(intent_login);

        }


//        Toast.makeText(context,""+string_emi,Toast.LENGTH_LONG).show();

//        Toast.makeText(context,""+string_amount+","+string_days+","+string_emi,Toast.LENGTH_LONG).show();

        initializeView();

    }

    private void initializeView() {
        relativeLayout = findViewById(R.id.id_amount_layout);
        button_get_money = findViewById(R.id.id_activity_amount_details_btn);
        linearLayout_amount_bg = findViewById(R.id.id_amount_details_LL);
        textView_amount = findViewById(R.id.id_amount_details_amount_txtVW);
        textView_days = findViewById(R.id.id_amount_details_days_txtVW);
        textView_disbursal_amount = findViewById(R.id.id_amount_details_disbursal_amount_txtVW);
        textView_active_amount = findViewById(R.id.id_amount_details_active_amount_txtVW);
        textView_processing_fee = findViewById(R.id.id_amount_details_processing_fee_txtVW);
        textView_gst = findViewById(R.id.id_amount_details_gst_txtVW);
        textView_total_interest = findViewById(R.id.id_amount_details_total_interest_txtVW);
        textView_annual_interest = findViewById(R.id.id_amount_details_annual_interest_txtVW);

        textView_amount.setText(string_amount);
        int processing_fee = Integer.parseInt(string_amount) * 9 / 100;
        textView_processing_fee.setText(String.valueOf(processing_fee));
        int gst = processing_fee * 18 / 100;
        textView_gst.setText(String.valueOf(gst));
        double total_interest = Double.parseDouble(string_amount) * 1.48 / 100;
        int int_total_interest = (int) Math.round(total_interest);
        textView_total_interest.setText(String.valueOf(int_total_interest));

        textView_active_amount.setText(string_amount);
        textView_days.setText(string_days + " Days");

        int int_total_interes = Integer.parseInt(textView_total_interest.getText().toString());
        int less_amount = processing_fee + int_total_interes;
        int disbursal_amount = Integer.parseInt(string_amount) - less_amount;

//        int final_disbursal_amount= (int) Math.round(disbursal_amount);
        textView_disbursal_amount.setText(String.valueOf(disbursal_amount));

        onClickViews();


    }

    private void onClickViews() {
        button_get_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                volleyCheckDetailsRequest();
                dialogProgressBar.showDialog();
                volleySendBankDetailsRequest();
                string_disbursal_amount = textView_disbursal_amount.getText().toString();
                string_active_amount = textView_active_amount.getText().toString();
                string_processing_fee = textView_processing_fee.getText().toString();
                string_gst = textView_gst.getText().toString();
                string_total_interest = textView_total_interest.getText().toString();
                string_annual_interest = textView_annual_interest.getText().toString();

            }
        });
    }

    private void volleySendBankDetailsRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.BANK_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("BankDetails_response" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String response_message = jsonObject.getString("message");

                    if (response_message.equalsIgnoreCase("Successfully Submitted")) {
                        Toast.makeText(context, "Successfully Submitted Amount details!!", Toast.LENGTH_LONG).show();

//                        volleyCheckDetailsRequest();
                        Intent intent = new Intent(context, KYCActivity.class);
                        intent.putExtra("mobile_number", intent_mobile_number);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

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
                System.out.println("BankDetails_error" + error.getLocalizedMessage());
                Toast.makeText(context, "Sorry!Server Error!", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("amount", string_amount);
                params.put("days", string_days);
                params.put("disbursal_amount", string_disbursal_amount);
                params.put("active_amount", string_active_amount);
                params.put("processing_fee", string_processing_fee);
                params.put("gst", string_gst);
                params.put("total_interest", string_total_interest);
                params.put("annual_interest", "36");
                params.put("phone", string_mobile_number);
                System.out.println("BankDetails_params:" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "Bank_Details");
    }

    private void volleyCheckDetailsRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.CHECK_DETAILS_SUBMITTED + "?phone=" + intent_mobile_number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialogProgressBar.hideDialog();

                System.out.println("check_details_response:" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String string_kyc = jsonObject.getString("kyc");
                        String string_reference = jsonObject.getString("reference");
                        String string_account = jsonObject.getString("account");

                        if (!string_kyc.contains("KYC Details Submitted")) {
                            Intent intent = new Intent(context, KYCActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (!string_account.contains("Account Details Submitted")) {
                            Intent intent = new Intent(context, BankDetailsActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            startActivity(intent);
                        } else if (!string_reference.contains("Reference Details Submitted")) {
                            Intent intent = new Intent(context, ReferenceActivity.class);
                            intent.putExtra("mobile_number", intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Fragment fragment = new SuccessFulFragment();
                            loadResponseFragment(fragment);

/*
                            Intent intent=new Intent(context,KYCActivity.class);
                            intent.putExtra("mobile_number",intent_mobile_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
*/
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                System.out.println("check_details_error" + error.getLocalizedMessage());
                Toast.makeText(context, "Sorry!Server Error!", Toast.LENGTH_LONG).show();

            }
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("phone",string_mobile_number);
                return params;
            }
        }*/;
        AppController.getInstance().addToRequestQueue(stringRequest, "check_Details_Submitted");
    }

    private void loadResponseFragment(Fragment fragment) {
        relativeLayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("mobile_number", intent_mobile_number);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_amount_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
