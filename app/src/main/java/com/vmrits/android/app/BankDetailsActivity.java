package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class BankDetailsActivity extends AppCompatActivity {
    private Button button_submit;
    private Context context = BankDetailsActivity.this;
    private EditText editText_account_name, editText_bank_name, editText_account_number, editText_branch_ifsc, editText_branch_name;
    private String string_account_name, string_bank_name, string_account_number, string_branch_ifsc, string_branch_name, intent_mobile_number;
    private DialogProgressBar dialogProgressBar;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        dialogProgressBar = new DialogProgressBar(context);
        dialogProgressBar.dialogInit();

        Intent intent = getIntent();
        intent_mobile_number = intent.getStringExtra("mobile_number");

        initializeViews();
    }

    private void initializeViews() {
        button_submit = findViewById(R.id.id_bank_details_submit_btn);
        editText_account_name = findViewById(R.id.id_bank_details_account_name_edt);
        editText_bank_name = findViewById(R.id.id_bank_details_bank_name_edt);
        editText_account_number = findViewById(R.id.id_bank_details_account_number_edt);
        editText_branch_ifsc = findViewById(R.id.id_bank_details_ifsc_edt);
        editText_branch_name = findViewById(R.id.id_bank_details_branch_name_edt);
        relativeLayout = findViewById(R.id.id_bank_details_layout);

        onClickViews();
    }

    private void onClickViews() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProgressBar.showDialog();
                string_account_name = editText_account_name.getText().toString();
                string_bank_name = editText_bank_name.getText().toString();
                string_account_number = editText_account_number.getText().toString();
                string_branch_ifsc = editText_branch_ifsc.getText().toString();
                string_branch_name = editText_branch_name.getText().toString();
                if (!TextUtils.isEmpty(string_account_name) && !TextUtils.isEmpty(string_account_number) &&
                        !TextUtils.isEmpty(string_bank_name) && !TextUtils.isEmpty(string_branch_ifsc) && !TextUtils.isEmpty(string_branch_name)) {
                    volleyBankDetailsSubmitRequest();
/*
                    Intent intent = new Intent(context, ReferenceActivity.class);
                    startActivity(intent);
*/
                } else {
                    Toast.makeText(context, "Please Provide All Details!!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void volleyBankDetailsSubmitRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.BANK_ACCOUNT_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("bank_details_response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String response_message = jsonObject.getString("message");
                    if (response_message.equalsIgnoreCase("Successfully Submitted")) {
                        dialogProgressBar.hideDialog();
                        Toast.makeText(context, "Successful! Bank details Submitted!!", Toast.LENGTH_LONG).show();
//                        volleyCheckDetailsRequest();
                        Intent intent = new Intent(context, ReferenceActivity.class);
                        intent.putExtra("mobile_number", intent_mobile_number);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Failed! Bank details not Submitted!!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                Toast.makeText(context, "Sorry!!Server Error!!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ac_holder_name", string_account_name);
                params.put("acc_no", string_account_number);
                params.put("bank_name", string_bank_name);
                params.put("ifsc", string_branch_ifsc);
                params.put("branch_name", string_branch_name);
                params.put("phone", intent_mobile_number);
                System.out.println("bank_details_params:" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "Bank_details");
    }

    private void volleyCheckDetailsRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.CHECK_DETAILS_SUBMITTED + "?phone=" + intent_mobile_number, new com.android.volley.Response.Listener<String>() {
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
                params.put("phone",intent_mobile_number);
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
        fragmentTransaction.replace(R.id.id_bank_container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
