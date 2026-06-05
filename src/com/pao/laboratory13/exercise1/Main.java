package com.pao.laboratory13.exercise1;

public class Main {
    enum State {
        INIT, AUTH, OPEN, CLOSED
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int q = scanner.nextInt();
        scanner.nextLine(); // consume newline

        State state = State.INIT;
        int historyCount = 0;
        String currentUser = null;

        int processed = 0;
        while (processed < q && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            processed++;

            String[] tokens = line.split("\\s+", 2);
            String command = tokens[0];

            switch (command) {
                case "AUTH":
                    if (tokens.length < 2) {
                        System.out.println("ERR E_PARSE AUTH");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else {
                        currentUser = tokens[1];
                        state = State.AUTH;
                        historyCount = 0;
                        System.out.println("OK AUTH user=" + currentUser);
                    }
                    break;

                case "OPEN":
                    if (tokens.length > 1) {
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
                    if (tokens.length < 2) {
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
                    if (tokens.length < 2) {
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
                    if (tokens.length > 1) {
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
                    if (tokens.length > 1) {
                        System.out.println("ERR E_PARSE CLOSE");
                    } else if (state == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (state == State.INIT || state == State.AUTH) {
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
