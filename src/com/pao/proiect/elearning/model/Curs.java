package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Curs implements Comparable<Curs> {
    private int id;
    private String titlu;
    private List<Material> materiale = new ArrayList<>();

    public Curs(int id, String titlu) {
        this.id = id;
        this.titlu = titlu;
    }

    public int getId() { return id; }
    public String getTitlu() { return titlu; }
    public void adaugaMaterial(Material m) { materiale.add(m); }

    public void afiseazaMateriale() {
        if (materiale.isEmpty()) {
            System.out.println("   [Cursul nu are materiale inca]");
        } else {
            System.out.println("   Materiale incluse:");
            for (Material m : materiale) {
                System.out.print("     -> ");
                m.afiseazaInfo(); // Apeleaza polimorfic afisarea (Video sau Quiz)
            }
        }
    }

    @Override
    public int compareTo(Curs o) {
        return this.titlu.compareTo(o.titlu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Curs)) return false;
        Curs curs = (Curs) o;
        return id == curs.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Curs[" + id + "]: " + titlu;
    }
}