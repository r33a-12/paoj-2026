package com.pao.proiect.elearning.model;

public abstract class MembruPlatforma extends Utilizator {
    protected String dataInscriere;

    public MembruPlatforma(int id, String nume, String dataInscriere) {
        super(id, nume);
        this.dataInscriere = dataInscriere;
    }

    public String getDataInscriere() { return dataInscriere; }
}