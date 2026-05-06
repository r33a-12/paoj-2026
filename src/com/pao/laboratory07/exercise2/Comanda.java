package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected OrderState stare;
    protected String client;

    public Comanda(String nume) {
        this.nume = nume;
        this.stare = OrderState.PLACED;
    }

    public abstract double pretFinal();
    public abstract String descriere();

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
