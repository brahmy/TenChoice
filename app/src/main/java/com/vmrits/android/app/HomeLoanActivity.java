package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class HomeLoanActivity extends AppCompatActivity {
    private GridView gridView_home_loan;
    private ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList;
    private Context context = HomeLoanActivity.this;
    private HomeLoanPOJO homeLoanPOJO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loan);
        homeLoanPOJOArrayList = new ArrayList<>();

        initializeViews();
    }

    private void initializeViews() {
        gridView_home_loan = findViewById(R.id.id_home_loan_gridView);

//        loadHomeLoanData();

        initializeAdapter();
        getAmountListRequest();

        onClickViews();
    }

/*
    private void loadHomeLoanData() {
        homeLoanPOJO=new HomeLoanPOJO("1","3000","2","15");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("2","5000","2","15");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("3","7000","2","15");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("4","9500","2","15");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("5","10000","2","15");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("6","50000","4","60");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("7","75000","5","75");
        homeLoanPOJOArrayList.add(homeLoanPOJO);
        homeLoanPOJO=new HomeLoanPOJO("8","100000","10","150");
        homeLoanPOJOArrayList.add(homeLoanPOJO);

    }
*/

    private void initializeAdapter() {
        HomeLoadAdapter homeLoadAdapter = new HomeLoadAdapter(context, homeLoanPOJOArrayList);
        gridView_home_loan.setAdapter(homeLoadAdapter);
    }

    private void onClickViews() {
        gridView_home_loan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeLoanPOJO homeLoanPOJO = homeLoanPOJOArrayList.get(position);

                Intent intent = new Intent(context, AmountDetailsActivity.class);
                intent.putExtra("amount", homeLoanPOJO.getPrice());
                intent.putExtra("days", homeLoanPOJO.getDays());
                intent.putExtra("emi", homeLoanPOJO.getEMI());
                startActivity(intent);
            }
        });
    }

    private void getAmountListRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.AMOUNT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("amount_response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i < jsonObject.length(); i++) {
                        HomeLoanPOJO homeLoanPOJO = new HomeLoanPOJO();
                        homeLoanPOJO.setId("id");
                        homeLoanPOJO.setDays("amount");
                        homeLoanPOJO.setEMI("days");
                        homeLoanPOJO.setPrice("emi");
                        homeLoanPOJOArrayList.add(homeLoanPOJO);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    initializeAdapter();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sorry! Server Error!!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, "amount_list");
    }
}
