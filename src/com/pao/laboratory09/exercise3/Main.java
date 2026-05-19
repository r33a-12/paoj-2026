package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii();
        
        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);
        
        ProcessorThread processorRunnable = new ProcessorThread(coada);
        Thread processorThread = new Thread(processorRunnable);
        
        atm1.start();
        atm2.start();
        atm3.start();
        
        processorThread.start();
        
        atm1.join();
        atm2.join();
        atm3.join();
        
        processorRunnable.activ = false;
        synchronized (coada) {
            coada.notifyAll();
        }
        
        processorThread.join();
        
        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
