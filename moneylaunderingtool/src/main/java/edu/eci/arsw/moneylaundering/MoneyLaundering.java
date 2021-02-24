package edu.eci.arsw.moneylaundering;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering {
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private static int amountOfFilesTotal;
    private static AtomicInteger amountOfFilesProcessed;
    private int CantHilos = 5;
    private ThreadLaundering[] threadLaundering;
    private List<File> transactionFiles;
    private static MoneyLaundering moneyLaundering = new MoneyLaundering();
    static boolean isPaused=false;

    public MoneyLaundering() {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        amountOfFilesProcessed.set(0);
        transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
    }

    public void processTransactionData() {

        threadLaundering = new ThreadLaundering[CantHilos];
        int tamaños = amountOfFilesTotal / CantHilos;
        int residuo = amountOfFilesTotal % CantHilos;
        for (int i = 0; i < CantHilos; i++) {
            threadLaundering[i] = new ThreadLaundering(transactionReader, transactionAnalyzer, this);
        }
        int llevo = 0;
        for (File files : transactionFiles) {
            threadLaundering[llevo].addFile(files);
            if (llevo == CantHilos - 1) {
                llevo = 0;
            } else {
                llevo++;
            }
        }
        for (ThreadLaundering hilos : threadLaundering) {
            hilos.start();
        }
        for (ThreadLaundering hilos : threadLaundering) {
            try {
                hilos.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        System.out.println("Programa Finalizado");
        showReport();
        System.exit(0);
    }

    public List<String> getOffendingAccounts() {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }


    public static void main (String[]args) {
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        while (amountOfFilesProcessed.get() < amountOfFilesTotal) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.contains("exit")) {
                break;
            } else if (line.equals("") && !isPaused) {
                System.out.println("Programa pausado");
                isPaused = true;
                for (ThreadLaundering hilo : moneyLaundering.getHilos()) {
                    hilo.pause();
                }
                showReport();
            } else if (line.equals("") && isPaused) {
                isPaused = false;
                System.out.println("Programa reanudado");
                for (ThreadLaundering hilo : moneyLaundering.getHilos()) {
                    hilo.reanudar();
                }
            }
        }
        showReport();

    }

    private static void showReport(){
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2) -> s1 + "\n" + s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }


    private ThreadLaundering[] getHilos () {
        return threadLaundering;
    }


    public void processArchive () {
        amountOfFilesProcessed.incrementAndGet();
    }
}

