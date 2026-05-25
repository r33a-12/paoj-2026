package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> coada = new LinkedList<>();
    private final int capacitate = 5;

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == capacitate) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        coada.add(t);
        System.out.printf(java.util.Locale.US, "[ATM-%d] trimite: Tranzactie #%d %.2f RON\n", atmId, t.id, t.suma);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
            if (coada.isEmpty()) {
                return null; // Spurious wakeup or shutdown notification
            }
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean areElemente() {
        return !coada.isEmpty();
    }
}
