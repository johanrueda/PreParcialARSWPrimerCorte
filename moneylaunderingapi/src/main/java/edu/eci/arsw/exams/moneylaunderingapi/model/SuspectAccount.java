package edu.eci.arsw.exams.moneylaunderingapi.model;

import java.util.concurrent.atomic.AtomicInteger;

public class SuspectAccount {
    public String accountId;
    public AtomicInteger amountOfSmallTransactions;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getAmountOfSmallTransactions() {
        return amountOfSmallTransactions.get();
    }

    public void setAmountOfSmallTransactions(AtomicInteger amountOfSmallTransactions) {
        this.amountOfSmallTransactions = amountOfSmallTransactions;
    }


    public void addTransactions(int newTransactions){
        amountOfSmallTransactions.getAndAdd(newTransactions);
    }

    public SuspectAccount(String accountId, int amountOfSmallTransactions){
        this.accountId=accountId;
        this.amountOfSmallTransactions = new AtomicInteger(amountOfSmallTransactions);
    }
}
