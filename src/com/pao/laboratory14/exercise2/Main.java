package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        EvenimentRepository repo = new EvenimentRepository();
        repo.initSchema();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.next();
            try {
                if (cmd.equals("ADD")) {
                    String nume = scanner.next();
                    String data = scanner.next();
                    int cap = scanner.nextInt();
                    TipBilet tip = TipBilet.valueOf(scanner.next());

                    Eveniment ev = new Eveniment(0, nume, data, cap, tip);
                    repo.save(ev);
                    System.out.println("Adaugat: [" + ev.getId() + "] " + ev.getNume());
                } else if (cmd.equals("LIST")) {
                    for (Eveniment ev : repo.findAll()) {
                        System.out.println(ev);
                    }
                } else if (cmd.equals("DELETE")) {
                    int id = scanner.nextInt();
                    int deleted = repo.deleteImpl(id);
                    if (deleted > 0) {
                        System.out.println("Sters: " + id);
                    } else {
                        System.out.println("Nu exista: " + id);
                    }
                } else if (cmd.equals("COUNT")) {
                    System.out.println("Total: " + repo.count());
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
