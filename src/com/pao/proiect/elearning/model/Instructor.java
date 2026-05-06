package com.pao.proiect.elearning.model;

public class Instructor extends MembruPlatforma {
    private String specializare;

    public Instructor(int id, String nume, String dataInscriere, String specializare) {
        super(id, nume, dataInscriere);
        this.specializare = specializare;
    }

    @Override
    public String getRol() {
        return "INSTRUCTOR";
    }
}