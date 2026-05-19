package com.pao.laboratory09;

import com.pao.laboratory09.exercise1.Main;
import java.io.*;

public class TestEx1 {
    public static void main(String[] args) throws Exception {
        String input = "3\n" +
                "1 1500.00 2024-01-15 RO01SRC1 RO01DST1 CREDIT\n" +
                "2 750.50 2024-01-22 RO02SRC2 RO02DST2 DEBIT\n" +
                "3 200.00 2024-02-05 RO01SRC1 RO03DST3 CREDIT\n" +
                "LIST\n";
        
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[0]);
    }
}
