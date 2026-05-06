package com.pao.proiect.elearning.model;

public final class ScorFinal {
    private final String idReferinta;
    private final int punctaj;

    public ScorFinal(String idReferinta, int punctaj) {
        this.idReferinta = idReferinta;
        this.punctaj = punctaj;
    }

    public String getIdReferinta() { return idReferinta; }
    public int getPunctaj() { return punctaj; }

    @Override
    public String toString() {
        return "Scor: " + punctaj + " (Ref: " + idReferinta + ")";
    }
}