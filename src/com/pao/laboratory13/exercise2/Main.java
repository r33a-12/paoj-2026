package com.pao.laboratory13.exercise2;

import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;

public class Main {
    
    // Motorul de protocol adaptat pentru a fi folosit per-sesiune
    static class ProtocolEngine {
        enum State { INIT, AUTH, OPEN, CLOSED }
        private State state = State.INIT;
        private int historyCount = 0;

        public String processCommand(String line) {
            String[] parts = line.trim().split("\\s+", 2);
            String cmd = parts[0];
            String payload = parts.length > 1 ? parts[1].trim() : "";
            
            switch (cmd) {
                case "AUTH":
                    if (payload.isEmpty()) return "ERR E_PARSE AUTH";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    state = State.AUTH;
                    historyCount = 0;
                    return "OK AUTH user=" + payload;
                case "OPEN":
                    if (!payload.isEmpty()) return "ERR E_PARSE OPEN";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    if (state == State.OPEN) return "ERR E_STATE ALREADY_OPEN";
                    if (state == State.INIT) return "ERR E_STATE NOT_OPEN";
                    state = State.OPEN;
                    return "OK OPEN";
                case "SEND":
                    if (payload.isEmpty()) return "ERR E_PARSE SEND";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
                    historyCount++;
                    return "OK OPEN sent";
                case "BROADCAST":
                    if (payload.isEmpty()) return "ERR E_PARSE BROADCAST";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
                    historyCount++;
                    return "OK OPEN broadcast";
                case "HISTORY":
                    if (!payload.isEmpty()) return "ERR E_PARSE HISTORY";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
                    return "OK OPEN history=" + historyCount;
                case "CLOSE":
                    if (!payload.isEmpty()) return "ERR E_PARSE CLOSE";
                    if (state == State.CLOSED) return "ERR E_STATE CLOSED";
                    if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
                    state = State.CLOSED;
                    return "OK CLOSED";
                default:
                    return "ERR E_PARSE UNKNOWN_COMMAND";
            }
        }
    }

    // Gestionarea sesiunii per client pe server
    static class SessionHandler implements Runnable {
        private Socket socket;
        public SessionHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try (
                Socket s = socket;
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true)
            ) {
                ProtocolEngine engine = new ProtocolEngine();
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String response = engine.processCommand(line);
                    out.println(response);
                    if (response.equals("OK CLOSED")) {
                        break; // starea e terminala
                    }
                }
            } catch (IOException e) {
                System.out.println("[SERVER-ERR] Exception in session handler: " + e.getMessage());
            }
        }
    }

    // Client care se conecteaza la server, ruleaza comenzi si se deconecteaza
    static class ClientRunner implements Runnable {
        private String name;
        private int port;
        private String user;
        private CountDownLatch latch;
        
        public ClientRunner(String name, int port, String user, CountDownLatch latch) {
            this.name = name;
            this.port = port;
            this.user = user;
            this.latch = latch;
        }
        
        @Override
        public void run() {
            try (
                Socket socket = new Socket("127.0.0.1", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                System.out.println("[" + name + "] Connected");
                String[] commands = {
                    "AUTH " + user,
                    "OPEN",
                    "SEND hi from " + user,
                    "BROADCAST x",
                    "HISTORY",
                    "CLOSE"
                };
                
                for (String cmd : commands) {
                    out.println(cmd);
                    String response = in.readLine();
                    System.out.println("[" + name + "] >> " + cmd + "  =>  " + response);
                    Thread.sleep((long) (Math.random() * 200 + 50)); // delay mic pentru a amesteca vizual logurile
                }
                System.out.println("[" + name + "] Disconnected");
            } catch (Exception e) {
                System.out.println("[" + name + "-ERR] " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9000;
        int numberOfClients = 2;
        CountDownLatch latch = new CountDownLatch(numberOfClients);
        
        // 1. Start server thread
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("[SERVER] Listening on port " + port);
                serverSocket.setSoTimeout(1000); // 1 secunda timeout ca sa verificam latch-ul periodic
                
                while (latch.getCount() > 0) {
                    try {
                        Socket socket = serverSocket.accept();
                        new Thread(new SessionHandler(socket)).start();
                    } catch (SocketTimeoutException e) {
                        // ignoram timeout-ul, doar ne permite sa re-verificam conditia din while
                    }
                }
                System.out.println("[SERVER] All clients done. Shutting down.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        
        // Asteptam ca serverul sa porneasca
        Thread.sleep(500);
        
        // 2. Start clients
        Thread client1 = new Thread(new ClientRunner("CLIENT-1", port, "alice", latch));
        Thread client2 = new Thread(new ClientRunner("CLIENT-2", port, "bob", latch));
        
        client1.start();
        client2.start();
        
        // 3. Asteptam terminarea clientilor si a serverului
        client1.join();
        client2.join();
        serverThread.join();
    }
}
