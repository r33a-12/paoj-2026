package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) throws Exception {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();
                    students.add(new Student(nume, varsta, new Adresa(oras, strada)));
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        int prag = 0;
        if (scanner.hasNextInt()) {
            prag = scanner.nextInt();
        }
        scanner.close();

        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getVarsta() >= prag) {
                filteredStudents.add(student);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Student student : filteredStudents) {
                bw.write(student.toString());
                bw.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filteredStudents.size() + " studenti");
        System.out.println();
        for (Student student : filteredStudents) {
            System.out.println(student);
        }
        System.out.println();
        System.out.println("Scris in: " + OUTPUT_FILE);
    }
}
