package com.pao.proiect.elearning.model;

public abstract class Material {
    protected String titlu;

    public Material(String titlu) {
        this.titlu = titlu;
    }

    public abstract void afiseazaInfo();
}