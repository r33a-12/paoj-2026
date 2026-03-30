package com.pao.laboratory03.exercise;

import com.pao.laboratory03.exercise.Student;
import com.pao.laboratory03.exercise.Subject;
import java.util.*;

public class StudentService {
    // singleton
    private static StudentService instance;
    private final List<Student> students;

    // constructor
    private StudentService() {
        this.students = new ArrayList<>();
    }

    // obtinerea instantei
    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    // a)
    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul cu numele " + name + " există deja!");
            }
        }
        students.add(new Student(name, age));
    }

    // b)
    public Student findByName(String name) {
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("StudentNotFoundException: " + name));
    }

    // c)
    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    // d)
    public void printAllStudents() {
        System.out.println("--- Listă Studenți ---");
        students.forEach(System.out::println);
    }

    // e)
    public void printTopStudents() {
        System.out.println("--- Top Studenți (Descrescător după medie) ---");
        List<Student> sortedStudents = new ArrayList<>(students);

        sortedStudents.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        sortedStudents.forEach(System.out::println);
    }

    // f)
    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student s : students) {
            for (Map.Entry<String, Double> entry : s.getGrades().entrySet()) {
                Subject sub = Subject.valueOf(entry.getKey());
                Double grade = entry.getValue();

                sums.put(sub, sums.getOrDefault(sub, 0.0) + grade);
                counts.put(sub, counts.getOrDefault(sub, 0) + 1);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Subject sub : sums.keySet()) {
            averages.put(sub, sums.get(sub) / counts.get(sub));
        }

        return averages;
    }
}
