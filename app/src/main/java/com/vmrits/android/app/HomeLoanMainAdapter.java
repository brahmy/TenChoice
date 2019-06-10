package com.vmrits.android.app;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeLoanMainAdapter extends RecyclerView.Adapter<HomeLoanMainAdapter.HomeLoanMainViewHolder> {
    private ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] mColors = {"#673AB7", "#D32F2F", "#C2185B", "#7B1FA2", "#7C4DFF", "#536DFE", "#448AFF", "#FF3D00", "#4E342E", "#424242", "#37474F", "#00BCD4", "#1B5E20", "#7C4DFF"};

    public HomeLoanMainAdapter(Context context, ArrayList<HomeLoanPOJO> homeLoanPOJOArrayList) {
        this.context = context;
        this.homeLoanPOJOArrayList = homeLoanPOJOArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HomeLoanMainAdapter.HomeLoanMainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_home_loan_main, viewGroup, false);
        return new HomeLoanMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeLoanMainAdapter.HomeLoanMainViewHolder homeLoanMainViewHolder, int i) {
        HomeLoanPOJO homeLoanPOJO = homeLoanPOJOArrayList.get(i);
        homeLoanMainViewHolder.textView_amount.setText("Rs. " + homeLoanPOJO.getPrice());
        homeLoanMainViewHolder.textView_days.setText(homeLoanPOJO.getDays() + " Days");

//        if (!homeLoanPOJO.getEMI().contains("0")) {
        homeLoanMainViewHolder.textView_emi.setText(homeLoanPOJO.getEMI() + " EMI");
        /*}else{
            homeLoanMainViewHolder.textView_emi.setText("0 EMI");
        }*/
        for (int j = 0; j < mColors.length; j++) {
            homeLoanMainViewHolder.cardView.setBackgroundColor(Color.parseColor(mColors[i]));

        }

/*
        switch (i){
            case 0:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.green));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 1:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.browen));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.purple));
                break;
            case 2:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.teal));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.teal));
                break;
            case 3:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.cyan));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.cyan));
                break;
            case 4:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.amber));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.amber));
                break;
            case 5:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 6:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.blue));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.blue));
                break;
            case 7:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.deep_orange));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.deep_orange));
                break;
            case 8:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.green));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 9:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.browen));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.purple));
                break;
            case 10:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.teal));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.teal));
                break;
            case 11:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.cyan));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.cyan));
                break;
            case 12:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.amber));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.amber));
                break;
            case 13:
                homeLoanMainViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
//                homeLoanMainViewHolder.textView_apply_me.setTextColor(context.getResources().getColor(R.color.red));
                break;

*/
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
*//*



        }
*/


    }

    @Override
    public int getItemCount() {
//        Toast.makeText(context,""+homeLoanPOJOArrayList.size(),Toast.LENGTH_LONG).show();
        return homeLoanPOJOArrayList.size();
    }

    public class HomeLoanMainViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_days, textView_amount, textView_apply_me, textView_emi;
        private CardView cardView;

        public HomeLoanMainViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.id_adapter_home_loan_main_card_view);
            textView_days = itemView.findViewById(R.id.id_adapter_home_loan_main_days_txtVW);
            textView_amount = itemView.findViewById(R.id.id_adapter_home_loan_main_amount_txtVW);
            textView_emi = itemView.findViewById(R.id.id_adapter_home_loan_main_emi_txtVW);
            textView_apply_me = itemView.findViewById(R.id.id_adapter_home_loan_main_apply_txtVW);
        }
    }
}
