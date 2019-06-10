package com.vmrits.android.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private Button button_next;
    private EditText editText_full_name, editText_father_name, editText_mother_name, editText_mobile_number, editText_dob, editText_income, editText_present_address, editText_perminent_address;
    private Switch aSwitch;
    private RadioGroup radioGroup_gender;
    private boolean gender;
    private String intent_mobile_number, string_full_name, string_father_name, string_mother_name, string_dob, string_montly_income,
            string_present_address, string_perminent_address, string_mobile_number, string_gender;
    private final Calendar myCalendar = Calendar.getInstance();
    private String dobFormat = "dd/MM/yy";
    private DatePickerDialog mDatePickerDialog;
    private Context context = SignUpActivity.this;
    private LoginSessionManager loginSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loginSessionManager = new LoginSessionManager(getApplicationContext());

        Intent intent = getIntent();
        intent_mobile_number = intent.getStringExtra("mobile_number");
        initializeViews();
    }

    private void initializeViews() {
        button_next = findViewById(R.id.id_fragment_sign_in_one_next_btn);
        editText_full_name = findViewById(R.id.id_activity_sign_in_one_full_name_edt);
        editText_father_name = findViewById(R.id.id_activity_sign_in_one_father_name);
        editText_mother_name = findViewById(R.id.id_activity_sign_in_one_mother_name);
        editText_dob = findViewById(R.id.id_activity_sign_in_one_DOB_edt);
        editText_income = findViewById(R.id.id_activity_sign_in_one_income_edt);
        editText_present_address = findViewById(R.id.id_activity_sign_in_one_present_address_edt);
        editText_perminent_address = findViewById(R.id.id_activity_sign_in_one_perminent_address_edt);
        editText_mobile_number = findViewById(R.id.id_activity_sign_in_one_mobile_number);

        aSwitch = findViewById(R.id.id_fragment_sign_in_one_switch);

        radioGroup_gender = findViewById(R.id.id_activity_sign_in_one_gender_RG);

        editText_mobile_number.setText(intent_mobile_number);
        onClickViews();
        setDateTimeField();
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                string_dob = sd.format(startDate);
                editText_dob.setText(string_dob);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void onClickViews() {
        editText_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText_dob.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        mDatePickerDialog.show();
                        return true;
                    }
                }
                return false;
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(editText_present_address.getText().toString())) {
                        editText_perminent_address.setText(editText_present_address.getText().toString());
                    } else {
                        editText_present_address.setError("Present address is empty!");
                    }
                }
            }
        });
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_activity_sign_in_one_male_RB:
                        gender = true;
                        string_gender = "M";
                        break;
                    case R.id.id_activity_sign_in_one_female_RB:
                        gender = false;
                        string_gender = "F";
                        break;
                }
            }
        });
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_full_name = editText_full_name.getText().toString();
                string_father_name = editText_father_name.getText().toString();
                string_mother_name = editText_mother_name.getText().toString();
                string_mobile_number = editText_mobile_number.getText().toString();
                string_dob = editText_dob.getText().toString();
                string_montly_income = editText_income.getText().toString();
                string_present_address = editText_present_address.getText().toString();
                string_perminent_address = editText_perminent_address.getText().toString();

                if (!TextUtils.isEmpty(string_full_name) && !TextUtils.isEmpty(string_father_name) && !TextUtils.isEmpty(string_mother_name)
                        && !TextUtils.isEmpty(string_dob) && !TextUtils.isEmpty(string_present_address) && !TextUtils.isEmpty(string_mobile_number)) {
                    volleySignUp();
                } else {
                    Toast.makeText(context, "Please Fill All Field!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void volleySignUp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUtility.SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("sing_up_response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String server_response = jsonObject.getString("message");
                    if (server_response.equalsIgnoreCase("Successfully Submitted")) {
                        loginSessionManager.createLoginSession(intent_mobile_number);
                        Toast.makeText(context, "Thank You For Registering with us!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, HomeLoanMainActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Sorry!Registration Failed!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("sing_up_error" + error.getMessage());
                Toast.makeText(context, "Sorry!Server Connection Failed!!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("full_name", string_full_name);
                params.put("father_name", string_father_name);
                params.put("mother_name", string_mother_name);
                params.put("phone", string_mobile_number);
                params.put("dob", string_dob);
                params.put("gender", string_gender);
                params.put("income", string_montly_income);
                params.put("address1", string_present_address);
                params.put("address2", string_perminent_address);
                System.out.println("sign_up_parameters" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "sign_up");

    }
}
