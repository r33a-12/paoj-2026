package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private CoadaTranzactii coada;
    
    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }
    
    @Override
    public void run() {
        try {
            while (activ) {
                Tranzactie t = coada.extrage();
                if (t != null) {
                    System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s\n", t.id, t.suma, t.data);
                    Thread.sleep(80);
                }
            }
        } catch (InterruptedException e) {
            // Fir oprit
        }
    }
}
