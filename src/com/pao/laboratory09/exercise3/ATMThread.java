package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread {
    private int id;
    private CoadaTranzactii coada;
    
    public ATMThread(int id, CoadaTranzactii coada) {
        this.id = id;
        this.coada = coada;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                int tranzactieId = id * 100 + i;
                double suma = 100.0 * i;
                String data = "2024-05-19";
                Tranzactie t = new Tranzactie(tranzactieId, suma, data);
                
                coada.adauga(t, id);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
