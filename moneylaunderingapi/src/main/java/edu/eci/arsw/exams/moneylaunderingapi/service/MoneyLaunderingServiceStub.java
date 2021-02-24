package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    private List<SuspectAccount> suspectAccounts = new CopyOnWriteArrayList<>();

    public MoneyLaunderingServiceStub(){
        suspectAccounts.add(new SuspectAccount("1",15));
        suspectAccounts.add(new SuspectAccount("2",20));
        suspectAccounts.add(new SuspectAccount("3",25));
        suspectAccounts.add(new SuspectAccount("4",30));
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        this.getAccountStatus(suspectAccount.getAccountId()).addTransactions(suspectAccount.getAmountOfSmallTransactions());
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        //TODO
        for(SuspectAccount suspectAccount: suspectAccounts){
            if(suspectAccount.getAccountId().equals(accountId)){
                return suspectAccount;
            }
        }
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        //TODO
        return suspectAccounts;
    }

    @Override
    public synchronized void addSuspect(SuspectAccount account) throws Exception {
        for (SuspectAccount suspectAccount:suspectAccounts){
            if(suspectAccount.getAccountId().equals(account.getAccountId())){
                throw new Exception("YA existe");
            }
        }
        suspectAccounts.add(account);
    }
}
