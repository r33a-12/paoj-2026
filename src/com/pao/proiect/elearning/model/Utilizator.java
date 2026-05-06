package com.pao.proiect.elearning.model;

import java.util.Objects;

public abstract class Utilizator {
    protected int id;
    protected String nume;

    public Utilizator(int id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    // Metodă abstractă cerută de ierarhie
    public abstract String getRol();

    public int getId() { return id; }
    public String getNume() { return nume; }

    @Override
    public String toString() {
        return "[" + getRol() + "] ID: " + id + ", Nume: " + nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}