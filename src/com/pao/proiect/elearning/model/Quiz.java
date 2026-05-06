package com.pao.proiect.elearning.model;

public class Quiz extends Material {
    private int nrIntrebari;

    public Quiz(String titlu, int nrIntrebari) {
        super(titlu);
        this.nrIntrebari = nrIntrebari;
    }

    @Override
    public void afiseazaInfo() {
        System.out.println("Quiz: " + titlu + " (" + nrIntrebari + " intrebari)");
    }
}