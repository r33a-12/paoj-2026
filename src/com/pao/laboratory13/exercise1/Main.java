package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    enum State {
        INIT, AUTH, OPEN, CLOSED
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int q = scanner.nextInt();
        scanner.nextLine();

        State state = State.INIT;
        int historyCount = 0;

        while (q > 0 && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            q--;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String payload = parts.length > 1 ? parts[1].trim() : "";

            switch (cmd) {
                case "AUTH":
                    if (payload.isEmpty()) {
                        System.out.println("ERR E_PARSE AUTH");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else {
                        state = State.AUTH;
                        historyCount = 0;
                        System.out.println("OK AUTH user=" + payload);
                    }
                    break;
                case "OPEN":
                    if (!payload.isEmpty()) {
                        System.out.println("ERR E_PARSE OPEN");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state == State.OPEN) {
                        System.out.println("ERR E_STATE ALREADY_OPEN");
                    } else if (state == State.INIT) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        state = State.OPEN;
                        System.out.println("OK OPEN");
                    }
                    break;
                case "SEND":
                    if (payload.isEmpty()) {
                        System.out.println("ERR E_PARSE SEND");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        historyCount++;
                        System.out.println("OK OPEN sent");
                    }
                    break;
                case "BROADCAST":
                    if (payload.isEmpty()) {
                        System.out.println("ERR E_PARSE BROADCAST");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        historyCount++;
                        System.out.println("OK OPEN broadcast");
                    }
                    break;
                case "HISTORY":
                    if (!payload.isEmpty()) {
                        System.out.println("ERR E_PARSE HISTORY");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        System.out.println("OK OPEN history=" + historyCount);
                    }
                    break;
                case "CLOSE":
                    if (!payload.isEmpty()) {
                        System.out.println("ERR E_PARSE CLOSE");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        state = State.CLOSED;
                        System.out.println("OK CLOSED");
                    }
                    break;
                default:
                    System.out.println("ERR E_PARSE UNKNOWN_COMMAND");
                    break;
            }
        }
    }
}
