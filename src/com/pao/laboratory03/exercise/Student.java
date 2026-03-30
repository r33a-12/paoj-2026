package com.pao.laboratory03.exercise;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    private Map<String, Double> grades;

    public Student(String name, int age) {
        // validare
        if (age < 18 || age > 60) {
            throw new RuntimeException("InvalidStudentException: Vârsta " + age + " este în afara intervalului 18-60.");
        }

        // initializare
        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    // gettere
    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<String, Double> getGrades() { return grades; }

    public void addGrade(Subject subject, double grade) {
        if (grade < 1 || grade > 10) {
            throw new RuntimeException("InvalidGradeException: Nota " + grade + " trebuie să fie între 1 și 10.");
        }
        grades.put(String.valueOf(subject), grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (double g : grades.values()) {
            sum += g;
        }
        return sum / grades.size();
    }

    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, avg=%.2f}",
                name, age, getAverage());
    }
}