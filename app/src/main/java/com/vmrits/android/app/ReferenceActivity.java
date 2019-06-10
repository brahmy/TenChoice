package com.vmrits.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferenceActivity extends AppCompatActivity {
    private EditText editText_name, editText_mobile_number, editText_address, editText_name_two, editText_mobile_number_two, editText_address_two;
    private Spinner spinner_relation, spinner_relation_two;
    private Button button_submit;
    private String[] relation_spinner = new String[]{"Reference Relation", "Father", "Mother", "Wife", "Son", "Daughter", "Friend", "Cousin", "Other"};
    private String intent_mobile_number, string_reference_name, string_relation, string_mobile_number, string_address;
    private RelativeLayout relativeLayout_reference;
    private Context context = ReferenceActivity.this;
    private LoginSessionManager loginSessionManager;
    private DialogProgressBar dialogProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        loginSessionManager = new LoginSessionManager(context);
        dialogProgressBar = new DialogProgressBar(context);
        dialogProgressBar.dialogInit();

        HashMap<String, String> getSessionDetails = loginSessionManager.getUserDetails();
        intent_mobile_number = getSessionDetails.get(LoginSessionManager.KEY_MOBILE_NUMBER);

        initializeViews();
    }

    private void initializeViews() {
        relativeLayout_reference = findViewById(R.id.id_reference_RR_layout);
        editText_name = findViewById(R.id.id_activity_reference_name_edt);
        editText_mobile_number = findViewById(R.id.id_activity_reference_mobile_number_edt);
        editText_address = findViewById(R.id.id_activity_reference_address_edt);
/*
        editText_name_two=findViewById(R.id.id_activity_reference_name_two_edt);
        editText_mobile_number_two=findViewById(R.id.id_activity_reference_mobile_number_two_edt);
        editText_address_two=findViewById(R.id.id_activity_reference_address_two_edt);
        spinner_relation_two=findViewById(R.id.id_activity_reference_relation_two_spinner);

*/

        spinner_relation = findViewById(R.id.id_activity_reference_relation_spinner);
        button_submit = findViewById(R.id.id_activity_reference_submit_btn);

        relativeLayout_reference.setVisibility(View.VISIBLE);
        spinnerInitialization();

        onClickViews();
    }

    private void onClickViews() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProgressBar.showDialog();
                string_reference_name = editText_name.getText().toString();
                string_mobile_number = editText_mobile_number.getText().toString();
                string_address = editText_address.getText().toString();
                string_relation = spinner_relation.getSelectedItem().toString();
                if (!TextUtils.isEmpty(string_reference_name) && !TextUtils.isEmpty(string_mobile_number) &&
                        !string_relation.equalsIgnoreCase("Reference Relation") && !TextUtils.isEmpty(string_address)) {
                    volleyReferenceRequest();
                } else {
                    Toast.makeText(context, "Please Fill All Details!!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void volleyReferenceRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.REFERENCE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("reference_response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String response_message = jsonObject.getString("message");
                    if (response_message.equalsIgnoreCase("Successfully Submitted")) {
                        dialogProgressBar.hideDialog();
                        Toast.makeText(context, "Successful! Reference details Submitted!!", Toast.LENGTH_LONG).show();
                        editText_mobile_number.setText("");
                        editText_address.setText("");
                        editText_name.setText("");

                        Fragment fragment = new SuccessFulFragment();
                        loadResponseFragment(fragment);

                    } else {
                        Toast.makeText(context, "Failed! Reference details not Submitted!!", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
                System.out.println("reference_error:" + error.getMessage());
                Toast.makeText(context, "Sorry!Server error!!", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ref_name", string_reference_name);
                params.put("ref_relation", string_relation);
                params.put("ref_phone", string_mobile_number);
                params.put("ref_address", string_address);
                params.put("phone", intent_mobile_number);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "reference_details");
    }

    private void loadResponseFragment(Fragment fragment) {
        relativeLayout_reference.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("mobile_number", intent_mobile_number);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void spinnerInitialization() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.adapterr_relation_reference, R.id.id_adapter_relation_reference_txtVW, relation_spinner);
        spinner_relation.setAdapter(arrayAdapter);

/*
        ArrayAdapter<String> arrayAdapter_two=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,relation_spinner);
        spinner_relation_two.setAdapter(arrayAdapter_two);
*/

    }
}
