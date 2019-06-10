package com.vmrits.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeLoanMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView_home_loan_main;
    private ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList;
    private HomeLoanPOJO homeLoanPOJO;
    private Context context = HomeLoanMainActivity.this;
    protected DialogProgressBar dialogProgressBar;
    private String string_mobile_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loan_main);
        homeLoanPOJOArrayList = new ArrayList<>();
        dialogProgressBar = new DialogProgressBar(context);
        Intent intent = getIntent();
        string_mobile_number = intent.getStringExtra("mobile_number");
        System.out.println("mopbile" + string_mobile_number);
//        string_mobile_number="9494604760";
        dialogProgressBar.dialogInit();
        initializeViews();
    }

    private void initializeViews() {
        recyclerView_home_loan_main = findViewById(R.id.id_home_loan_main_RV);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_home_loan_main.setLayoutManager(layoutManager);

        dialogProgressBar.showDialog();

//        volleyPaymentStatus();
        getAmountListRequest();


        onClickListener();

    }

    private void initializeAdapter() {
        HomeLoanMainAdapter homeLoanMainAdapter = new HomeLoanMainAdapter(context, homeLoanPOJOArrayList);
        recyclerView_home_loan_main.setAdapter(homeLoanMainAdapter);

    }

    private void onClickListener() {
        recyclerView_home_loan_main.addOnItemTouchListener(new RecyclerViewClickListener(context, recyclerView_home_loan_main, new RVClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeLoanPOJO homeLoanPOJO = homeLoanPOJOArrayList.get(position);
                Intent intent = new Intent(context, AmountDetailsActivity.class);
                intent.putExtra("amount", homeLoanPOJO.getPrice());
                intent.putExtra("days", homeLoanPOJO.getDays());
                intent.putExtra("emi", homeLoanPOJO.getEMI());
                intent.putExtra("mobile_number", string_mobile_number);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void volleyPaymentStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.PAYMENT_STATUS + "?phone=" + string_mobile_number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("payment_status:" + response);
//                Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();
                dialogProgressBar.hideDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String string_Status = jsonObject.getString("status");

/*                    if(string_Status.equalsIgnoreCase("Details Not Found")){
                        Intent intent = new Intent(context, HomeLoanMainActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    }else*/

                    if (string_Status.equalsIgnoreCase("pending")) {
                        Intent intent = new Intent(context, PaymentActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    } else if (string_Status.equalsIgnoreCase("approved")) {
                        //HomeLoanMainActivity
                        Intent intent = new Intent(context, PaymentActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

                    } else if (string_Status.equalsIgnoreCase("documents Not Submitted")) {
                        Intent intent = new Intent(context, KYCActivity.class);
                        intent.putExtra("mobile_number", string_mobile_number);
                        startActivity(intent);

//                        }

                    } else {
                        getAmountListRequest();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();
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
        AppController.getInstance().addToRequestQueue(stringRequest, "Payment_status");
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
    private void getAmountListRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUtility.AMOUNT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("amount_response" + response);
                dialogProgressBar.hideDialog();
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                Toast.makeText(context,""+jsonArray.length(),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HomeLoanPOJO homeLoanPOJO = new HomeLoanPOJO();
                        homeLoanPOJO.setId(jsonObject.getString("id"));
                        homeLoanPOJO.setPrice(jsonObject.getString("amount"));
                        homeLoanPOJO.setDays(jsonObject.getString("days"));
                        homeLoanPOJO.setEMI(jsonObject.getString("emi"));
//                    Toast.makeText(context,""+jsonObject.getString("emi"),Toast.LENGTH_LONG).show();
                        homeLoanPOJOArrayList.add(homeLoanPOJO);
                    }
                    initializeAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProgressBar.hideDialog();

                Toast.makeText(context, "Sorry! Server Error!!", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, "amount_list");
    }

}
