package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        
        File outFile = new File(OUTPUT_FILE);
        outFile.getParentFile().mkdirs();
        
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outFile))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                
                byte[] buffer = new byte[RECORD_SIZE];
                ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
                
                bb.putInt(id);
                bb.putDouble(suma);
                
                byte[] dataBytes = data.getBytes();
                for (int j = 0; j < 10; j++) {
                    if (j < dataBytes.length) {
                        buffer[12 + j] = dataBytes[j];
                    } else {
                        buffer[12 + j] = ' '; // pad cu spatii la dreapta
                    }
                }
                
                buffer[22] = (byte) (tip == TipTranzactie.CREDIT ? 0 : 1);
                buffer[23] = 0; // PENDING
                
                dos.write(buffer);
            }
        }
        
        try (RandomAccessFile raf = new RandomAccessFile(outFile, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();
                if (command.equals("READ")) {
                    int idx = scanner.nextInt();
                    raf.seek(idx * RECORD_SIZE);
                    byte[] buffer = new byte[RECORD_SIZE];
                    raf.readFully(buffer);
                    printRecord(idx, buffer);
                } else if (command.equals("UPDATE")) {
                    int idx = scanner.nextInt();
                    String statusStr = scanner.next();
                    byte status = 0;
                    if (statusStr.equals("PROCESSED")) status = 1;
                    else if (statusStr.equals("REJECTED")) status = 2;
                    
                    raf.seek(idx * RECORD_SIZE + 23);
                    raf.writeByte(status);
                    System.out.println("Updated [" + idx + "]: " + statusStr);
                } else if (command.equals("PRINT_ALL")) {
                    long len = raf.length();
                    int numRecords = (int) (len / RECORD_SIZE);
                    for (int i = 0; i < numRecords; i++) {
                        raf.seek(i * RECORD_SIZE);
                        byte[] buffer = new byte[RECORD_SIZE];
                        raf.readFully(buffer);
                        printRecord(i, buffer);
                    }
                }
            }
        }
    }

    private static void printRecord(int idx, byte[] buffer) {
        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
        int id = bb.getInt();
        double suma = bb.getDouble();
        
        StringBuilder dataSb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = (char) buffer[12 + i];
            if (c != ' ') {
                dataSb.append(c);
            }
        }
        String data = dataSb.toString();
        
        TipTranzactie tip = buffer[22] == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        
        String statusStr = "PENDING";
        if (buffer[23] == 1) statusStr = "PROCESSED";
        else if (buffer[23] == 2) statusStr = "REJECTED";
        
        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                idx, id, data, tip, suma, statusStr);
    }
}
