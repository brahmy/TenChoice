package com.vmrits.android.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignInFormOneFragment extends Fragment {
    private Button button_next;
    private EditText editText_full_name, editText_father_name, editText_mother_name, editText_dob, editText_income, editText_present_address, editText_perminent_address;
    private Switch aSwitch;
    private RadioGroup radioGroup_gender;
    private boolean gender;
    private String string_full_name, string_father_name, string_mother_name, string_dob, string_present_address, string_perminent_address;
    private final Calendar myCalendar = Calendar.getInstance();
    private String dobFormat = "dd/MM/yy";
    private DatePickerDialog mDatePickerDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_one, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        button_next = view.findViewById(R.id.id_fragment_sign_in_one_next_btn);
        editText_full_name = view.findViewById(R.id.id_activity_sign_in_one_full_name_edt);
        editText_father_name = view.findViewById(R.id.id_activity_sign_in_one_father_name);
        editText_mother_name = view.findViewById(R.id.id_activity_sign_in_one_mother_name);
        editText_dob = view.findViewById(R.id.id_activity_sign_in_one_DOB_edt);
        editText_income = view.findViewById(R.id.id_activity_sign_in_one_income_edt);
        editText_present_address = view.findViewById(R.id.id_activity_sign_in_one_present_address_edt);
        editText_perminent_address = view.findViewById(R.id.id_activity_sign_in_one_perminent_address_edt);

        aSwitch = view.findViewById(R.id.id_fragment_sign_in_one_switch);

        radioGroup_gender = view.findViewById(R.id.id_activity_sign_in_one_gender_RG);

        onClickViews();
        setDateTimeField();
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

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
                        break;
                    case R.id.id_activity_sign_in_one_female_RB:
                        gender = false;
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
                string_dob = editText_dob.getText().toString();
                string_present_address = editText_present_address.getText().toString();
                string_perminent_address = editText_perminent_address.getText().toString();

                if (!TextUtils.isEmpty(string_full_name) && !TextUtils.isEmpty(string_father_name) && !TextUtils.isEmpty(string_mother_name)
                        && !TextUtils.isEmpty(string_dob) && !TextUtils.isEmpty(string_present_address)) {
//                    loadFragmentNext();
                    Intent intent = new Intent(getActivity(), HomeLoanActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Please Fill All Field!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadFragmentNext() {
        Bundle bundle = new Bundle();
        bundle.putString("full_name", string_full_name);
        bundle.putString("father_name", string_father_name);
        bundle.putString("mother_name", string_mother_name);
        bundle.putString("dob", string_dob);
        bundle.putString("address", string_present_address);
        if (gender == true) {
            bundle.putString("gender", "1");
        } else {
            bundle.putString("gender", "0");

        }

        Fragment fragment = new SignInFormTwoFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.id_main_activity_fragment_container, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
