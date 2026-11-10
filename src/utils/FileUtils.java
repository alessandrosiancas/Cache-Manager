package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class FileUtils {

    // Lettura di un file con le chiamate delle risorse
    public static String[] readRequests(String fileName, int length) {
        String[] requests = new String[length];
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line = in.readLine();

            int i = 0; // indice di scorrimento di requests[0...n-1]

            // Ciclo per la lettura delle righe del file
            while (line != null && i < length) {

                // Creazione array senza gli spazi
                String[] sData = line.split(" ");

                // Inserimento nell'array delle richieste
                for (int j = 0; j < sData.length && i < length; j++) {
                    requests[i] = sData[j];
                    i++;
                }

                line = in.readLine(); // lettura prossima riga
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requests;
    }

    // Generatore di una sequenza (in file .txt)
    public static void createRandomTestFile(String fileName, int length, int numRes) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileName));
            Random random = new Random();

            for (int i = 0; i < length; i++) {
                String ri = "r" + random.nextInt(numRes);

                if (i == length - 1)
                    out.print(ri);
                else
                    out.print(ri + " ");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Il parametro append serve per stabilire se il file su cui si opera va
    // sovrascritto o concatenato a txt
    // append=false sovrascrive / append=true concatena
    public static void writeCsvLine(String fileName, String txt, boolean append) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileName, append));
            out.println(txt);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}