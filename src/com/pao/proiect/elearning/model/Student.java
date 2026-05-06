package com.pao.proiect.elearning.model;

public class Student extends MembruPlatforma {
    public Student(int id, String nume, String dataInscriere) {
        super(id, nume, dataInscriere);
    }

    @Override
    public String getRol() {
        return "STUDENT";
    }
}