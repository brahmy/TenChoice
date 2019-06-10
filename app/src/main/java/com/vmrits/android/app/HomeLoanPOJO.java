package com.vmrits.android.app;

public class HomeLoanPOJO {
    private String id;
    private String EMI;

/*
    public HomeLoanPOJO(String id, String amount,String emi, String days) {
        this.id=id;
        this.price=amount;
        this.EMI=emi;
        this.days=days;

    }
*/

    public String getEMI() {
        return EMI;
    }

    public void setEMI(String EMI) {
        this.EMI = EMI;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    private String days;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
}
