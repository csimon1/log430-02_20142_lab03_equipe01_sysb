/**
 * 
 */
package ca.etsmtl.log430.lab3.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Charly
 * 
 */
public final class FilesUtil {

    public final static boolean SameFiles(String file1, String file2) throws IOException {

        BufferedReader file1_Buffered = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
        BufferedReader file2_Buffered = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));

        boolean endReading = false;

        String file1_LineRead;
        String file2_LineRead;

        boolean sameFiles = false;

        while (!endReading) {
            file1_LineRead = file1_Buffered.readLine();
            file2_LineRead = file2_Buffered.readLine();

            if (file1_LineRead == null && file2_LineRead == null) {
                endReading = true;
                sameFiles = true;
                System.out.println("DEBUG : same files");
            } else if (file1_LineRead == null || file2_LineRead == null) {
                // if : like a XOR
                endReading = true;
                sameFiles = false;
                System.out.println("DEBUG : one file end first");
            } else {

                if (!file1_LineRead.equalsIgnoreCase(file2_LineRead)) {
                    endReading = true;
                    sameFiles = false;

                    System.out.println("DEBUG : line differents :");
                    System.out.println("DEBUG : \t" + file1_LineRead);
                    System.out.println("DEBUG : \t" + file2_LineRead);
                }
            }
        }

        file1_Buffered.close();
        file2_Buffered.close();

        return sameFiles;

    }

}
