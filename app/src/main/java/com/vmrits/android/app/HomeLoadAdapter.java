package com.vmrits.android.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeLoadAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList;
    private LayoutInflater layoutInflater;

    public HomeLoadAdapter(Context context, ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList) {
        this.context = context;
        this.homeLoanPOJOArrayList = homeLoanPOJOArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return homeLoanPOJOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeLoanPOJO homeLoanPOJO = homeLoanPOJOArrayList.get(position);
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.adapter_home_loan, null);
            viewHolder = new ViewHolder();
            viewHolder.textView_amount = view.findViewById(R.id.adapter_home_loan_amount_txtVW);
            viewHolder.textView_emi = view.findViewById(R.id.adapter_home_loan_emi_txtVW);
            viewHolder.textView_days = view.findViewById(R.id.adapter_home_loan_days_txtVW);
            viewHolder.textView_apply = view.findViewById(R.id.adapter_home_loan_apply_txtVW);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView_amount.setText("\u20A8" + homeLoanPOJO.getPrice());

        if (!homeLoanPOJO.getEMI().contains("2")) {
            viewHolder.textView_emi.setText(homeLoanPOJO.getEMI() + " EMI");
        }
        viewHolder.textView_days.setText(homeLoanPOJO.getDays() + " Days");

        //Is to ensure that the two columns
        int type = position / 2;
        //For each row processing respectively
        switch (position) {
            case 0:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.green));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 1:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.browen));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.purple));
                break;
            case 2:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.teal));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.teal));
                break;
            case 3:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.cyan));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.cyan));
                break;
            case 4:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.amber));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.amber));
                break;
            case 5:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.red));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 6:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.blue));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.blue));
                break;
            case 7:
                viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.deep_orange));
                viewHolder.textView_apply.setTextColor(context.getResources().getColor(R.color.deep_orange));
                break;
/*
        if(position%1==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else if(position%2==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.purple));
        }else if(position%3==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.cyan));
        }else if(position%4==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.teal));
        }else if(position%5==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.red));
        }else if(position%6==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.amber));
        }else if(position%7==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.blue));

        }else if(position%8==0){
            viewHolder.textView_amount.setBackgroundColor(context.getResources().getColor(R.color.deep_orange));
*/

        }


        return view;
    }

    private static class ViewHolder {
        private TextView textView_amount, textView_emi, textView_days, textView_apply;

    }
}
