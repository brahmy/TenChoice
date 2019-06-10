package com.vmrits.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SuccessFulFragment extends Fragment {
    private String string_mobile_number;
    private Button button_ok;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_successful, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        string_mobile_number = bundle.getString("mobile_number");
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        button_ok = view.findViewById(R.id.id_fragment_successfull_button);

        onClickViews();
/*
        new CountDownTimer(15000, 10000) {

            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(getActivity()!=null) {
                    getActivity().onBackPressed();

                }

            }
        }.start();
*/

    }

    private void onClickViews() {
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mobile_number", string_mobile_number);
                startActivity(intent);

            }
        });
    }
}
