package com.pao.proiect.elearning.model;

public class LectieVideo extends Material {
    private int durataMinute;

    public LectieVideo(String titlu, int durataMinute) {
        super(titlu);
        this.durataMinute = durataMinute;
    }

    @Override
    public void afiseazaInfo() {
        System.out.println("Video: " + titlu + " (" + durataMinute + " min)");
    }
}