package com.infy.stepDefinitions;


public class TestContext {
    private String depositAmount, balAmount;
    private String[] balanceAmount;

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }
    
    public String getBalanceAmount() {
        return balAmount;
    }

    public void setBalanceAmount(String[] balanceAmount) {
        this.balAmount = balanceAmount[0];
    }
    
    
}