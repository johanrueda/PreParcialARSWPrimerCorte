package edu.eci.arsw.moneylaundering;


import sun.awt.Mutex;

import java.util.ArrayList;
import java.io.File;
import java.util.List;


public class ThreadLaundering extends Thread {

    TransactionReader transactionReader;
    TransactionAnalyzer transactionAnalyzer;
    MoneyLaundering moneyLaundering;
    ArrayList<File> ListaArchivosLavado;
    Mutex mutex;
    boolean isPause;

    public  ThreadLaundering(TransactionReader excel, TransactionAnalyzer  excelAnalisis, MoneyLaundering cuentaLavado){
        this.transactionReader = excel;
        this.transactionAnalyzer = excelAnalisis;
        this. moneyLaundering =  cuentaLavado;
        this.ListaArchivosLavado=  new ArrayList<File>();
        mutex = new Mutex();
        isPause =false;
    }

    @Override
    public void run(){
        for(File archivo: ListaArchivosLavado){
            List<Transaction> transacciones = transactionReader.readTransactionsFromFile(archivo);
            for(Transaction transaction: transacciones) {
                while (isPause) {
                    synchronized (mutex) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    transactionAnalyzer.addTransaction(transaction);
                }

            }
            moneyLaundering.processArchive();
        }

    }


    public void addFile(File files) {
        ListaArchivosLavado.add(files);
    }

    public void pause() {
        isPause=true;
    }

    public void reanudar() {
        isPause=false;
        synchronized (mutex){
            mutex.notifyAll();
        }
    }
}
