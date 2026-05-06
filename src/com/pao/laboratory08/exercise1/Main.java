package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

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
        if (scanner.hasNextLine()) {
            String commandLine = scanner.nextLine().trim();
            String[] cmdParts = commandLine.split(" ", 2);
            String command = cmdParts[0].toUpperCase();

            if (command.equals("PRINT")) {
                for (Student student : students) {
                    System.out.println(student);
                }
            } else if (command.equals("SHALLOW") || command.equals("DEEP")) {
                if (cmdParts.length > 1) {
                    String targetName = cmdParts[1].trim();
                    Student targetStudent = null;
                    for (Student student : students) {
                        if (student.getNume().equals(targetName)) {
                            targetStudent = student;
                            break;
                        }
                    }

                    if (targetStudent != null) {
                        Student clonedStudent;
                        if (command.equals("SHALLOW")) {
                            clonedStudent = (Student) targetStudent.clone();
                        } else {
                            clonedStudent = targetStudent.deepClone();
                        }
                        
                        clonedStudent.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + targetStudent);
                        System.out.println("Clona: " + clonedStudent);
                    }
                }
            }
        }
        scanner.close();
    }
}
