package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class AllDetailsViewActivity extends AppCompatActivity {
    private EditText editText_full_name, editText_father_name, editText_mother_name, editText_dob, editText_present_address, editText_monthlyy_salary, editText_pan_number;
    private String string_full_name, string_father_name, string_mother_name, string_dob, string_present_address, string_salary, string_pan_number, string_gender, string_land, string_house;
    private RadioButton radioButton_house_yes, radioButton_house_no, radioButton_land_yes, radioButton_land_no;
    private Button button_confirm;
    private Context context = AllDetailsViewActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        intentData();
        initializeViews();
    }

    private void initializeViews() {
        editText_full_name = findViewById(R.id.id_activity_all_details_full_name_edt);
        editText_father_name = findViewById(R.id.id_activity_all_details__father_name);
        editText_mother_name = findViewById(R.id.id_activity_all_details_mother_name);
        editText_dob = findViewById(R.id.id_activity_all_details_DOB_edt);
        editText_present_address = findViewById(R.id.id_activity_all_details_present_address_edt);
        editText_monthlyy_salary = findViewById(R.id.id_activity_all_details_income_edt);
        editText_pan_number = findViewById(R.id.id_activity_all_details_pan_edt);
        radioButton_house_yes = findViewById(R.id.id_activity_all_details_house_yes_RB);
        radioButton_house_no = findViewById(R.id.id_activity_all_details_house_no_RB);
        radioButton_land_yes = findViewById(R.id.id_activity_all_details_land_yes_RB);
        radioButton_land_no = findViewById(R.id.id_activity_all_details_land_no_RB);
        button_confirm = findViewById(R.id.id_all_details_confirm_btn);

        if (string_land.contains("1")) {
            radioButton_house_yes.setChecked(true);
        } else {
            radioButton_house_no.setChecked(false);
        }
        if (string_house.contains("1")) {
            radioButton_land_yes.setChecked(true);

        } else {
            radioButton_land_no.setChecked(false);

        }

        editText_full_name.setText(string_full_name);
        editText_father_name.setText(string_father_name);
        editText_mother_name.setText(string_mother_name);
        editText_dob.setText(string_dob);
        editText_present_address.setText(string_present_address);
        editText_monthlyy_salary.setText(string_salary);
        editText_pan_number.setText(string_pan_number);

        onClickViews();
    }

    private void onClickViews() {
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KYCActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intentData() {
        Intent intent = getIntent();
        string_full_name = intent.getStringExtra("full_name");
        string_father_name = intent.getStringExtra("father_name");
        string_mother_name = intent.getStringExtra("mother_name");
        string_dob = intent.getStringExtra("dob");
        string_present_address = intent.getStringExtra("address");
        string_salary = intent.getStringExtra("salary");
        string_pan_number = intent.getStringExtra("pan");
        string_gender = intent.getStringExtra("gender");
        string_land = intent.getStringExtra("land");
        string_house = intent.getStringExtra("house");
    }
}
