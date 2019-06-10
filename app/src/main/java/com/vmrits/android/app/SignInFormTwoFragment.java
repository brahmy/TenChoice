package com.vmrits.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class SignInFormTwoFragment extends Fragment {
    private Spinner spinner_work;
    private Button button_submit;
    private String[] work_array = new String[]{"what you do", "Employee", "Business", "Former", "Others"};
    private String string_full_name, string_father_name, string_mother_name, string_dob, string_present_address, string_salary, string_pan_number, string_gender;
    private EditText editText_monthlyy_salary, editText_pan_number;
    private RadioGroup radioGroup_land, radioGroup_house;
    private int land, house;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_two, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        spinner_work = view.findViewById(R.id.id_activity_sign_in_two_work_spinner);
        editText_monthlyy_salary = view.findViewById(R.id.id_activity_sign_in_two_income_edt);
        editText_pan_number = view.findViewById(R.id.id_activity_sign_in_two_pan_edt);
        radioGroup_house = view.findViewById(R.id.id_activity_sign_in_two_land_RG);
        radioGroup_land = view.findViewById(R.id.id_activity_sign_in_two_house_RG);

        button_submit = view.findViewById(R.id.id_activity_sign_in_two_submit_btn);
        getFormData();
        onClickViews();

        SpinnerInitialization();
    }

    private void onClickViews() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_salary = editText_monthlyy_salary.getText().toString();
                string_pan_number = editText_pan_number.getText().toString();
                if (!TextUtils.isEmpty(string_salary) && !TextUtils.isEmpty(string_pan_number)) {
                    loadAllDetailsView();
                } else {
                    Toast.makeText(getActivity(), "Please Fill All Field!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        radioGroup_house.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_activity_sign_in_two_land_yes_RB:
                        land = 1;
                        break;
                    case R.id.id_activity_sign_in_two_land_no_RB:
                        land = 0;
                        break;
                }
            }
        });
        radioGroup_house.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_activity_sign_in_two_house_yes_RB:
                        house = 1;
                        break;
                    case R.id.id_activity_sign_in_two_house_no_RB:
                        house = 0;
                        break;
                }
            }
        });
    }

    private void loadAllDetailsView() {
        Intent intent = new Intent(getActivity(), AllDetailsViewActivity.class);
        intent.putExtra("full_name", string_full_name);
        intent.putExtra("father_name", string_father_name);
        intent.putExtra("mother_name", string_mother_name);
        intent.putExtra("dob", string_dob);
        intent.putExtra("address", string_present_address);
        intent.putExtra("gender", string_gender);
        intent.putExtra("salary", string_salary);
        intent.putExtra("pan", string_pan_number);

        if (house == 1) {
            intent.putExtra("house", "1");

        } else {
            intent.putExtra("house", "0");

        }
        if (land == 1) {
            intent.putExtra("land", "1");

        } else {
            intent.putExtra("land", "0");

        }
        startActivity(intent);
    }

    private void getFormData() {
        Bundle bundle = getArguments();
        assert bundle != null;
        string_full_name = bundle.getString("full_name");
        string_father_name = bundle.getString("father_name");
        string_mother_name = bundle.getString("mother_name");
        string_dob = bundle.getString("dob");
        string_present_address = bundle.getString("address");
        string_gender = bundle.getString("gender");
    }

    private void SpinnerInitialization() {
        ArrayAdapter<String> workSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, work_array);
        spinner_work.setAdapter(workSpinner);
    }
}
