package com.pao.laboratory14.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Bonus — Alocare Automata de Sali pentru Evenimente
 * <p>
 * Problema clasica de interviu: date N evenimente cu intervale [start, end],
 * gaseste numarul minim de sali necesare si atribuie fiecare eveniment la o sala.
 * <p>
 * Doua variante demonstrate:
 * Varianta 1 — greedy simplu O(N^2): prima sala disponibila
 * Varianta 2 — PriorityQueue O(N log N): min-heap de ore de final
 */
public class Main {

    record Eveniment(String nume, int startMin, int endMin) {
    }

    /**
     * Converteste "HH:MM" in minute intregi de la miezul noptii.
     */
    private static int toMin(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    /**
     * Converteste minute intregi inapoi in "HH:MM".
     */
    private static String toHHMM(int min) {
        return String.format("%02d:%02d", min / 60, min % 60);
    }

    public static void main(String[] args) {
        List<Eveniment> evenimente = new ArrayList<>(List.of(
                new Eveniment("Ev1", toMin("09:00"), toMin("11:00")),
                new Eveniment("Ev2", toMin("09:30"), toMin("10:30")),
                new Eveniment("Ev3", toMin("10:00"), toMin("12:00")),
                new Eveniment("Ev4", toMin("10:30"), toMin("13:00")),
                new Eveniment("Ev5", toMin("11:00"), toMin("14:00")),
                new Eveniment("Ev6", toMin("12:30"), toMin("15:00")),
                new Eveniment("Ev7", toMin("14:30"), toMin("16:00")),
                new Eveniment("Ev8", toMin("15:00"), toMin("17:00"))
        ));

        evenimente.sort(Comparator.comparingInt(Eveniment::startMin));

        System.out.println("=== Varianta 1: Greedy simplu O(N^2) ===");
        List<Integer> roomsEndTimes = new ArrayList<>();

        for (Eveniment ev : evenimente) {
            boolean foundRoom = false;
            for (int i = 0; i < roomsEndTimes.size(); i++) {
                if (roomsEndTimes.get(i) <= ev.startMin()) {
                    roomsEndTimes.set(i, ev.endMin());
                    System.out.printf("%-15s (%s - %s) -> Sala #%d\n", ev.nume(), toHHMM(ev.startMin()), toHHMM(ev.endMin()), i + 1);
                    foundRoom = true;
                    break;
                }
            }
            if (!foundRoom) {
                roomsEndTimes.add(ev.endMin());
                int newRoomIndex = roomsEndTimes.size();
                System.out.printf("%-15s (%s - %s) -> Sala #%d\n", ev.nume(), toHHMM(ev.startMin()), toHHMM(ev.endMin()), newRoomIndex);
            }
        }
        System.out.println("Sali minime utilizate (Var 1): " + roomsEndTimes.size());

        System.out.println("\n=== Varianta 2: PriorityQueue O(N log N) ===");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (Eveniment ev : evenimente) {
            if (!pq.isEmpty() && pq.peek() <= ev.startMin()) {
                pq.poll();
                pq.offer(ev.endMin());
            } else {
                pq.offer(ev.endMin());
            }
        }
        System.out.println("Sali minime utilizate (Var 2): " + pq.size());
    }
}

