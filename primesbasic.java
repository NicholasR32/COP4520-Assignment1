// Nicholas Rolland
// Assignment 1
// COP4520, Spring 2024, Prof. Juan Parra
// 1/26/2024

import java.util.*;
import java.io.*;

public class primesbasic
{
    public static void main(String[] args) throws IOException
    {
        // Handle IOExceptions with files
        try
        {
            // Delete old primes.txt and create new one
            File out = new File("primes.txt");
            if (out.exists()) out.delete();
            out.createNewFile();
            FileWriter writer = new FileWriter("primes.txt");

            // Record program running time
            final long start = System.nanoTime();
            printPrimesSlow(100000000, writer);
            writer.close();

            final long end = System.nanoTime();

            System.out.println("Runtime: " + (end - start) / 1000000.0 + "ms");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void printPrimesSlow(int upperbound, FileWriter writer) throws IOException
    {
        for (int i = 2; i <= upperbound; i++)
        {
            boolean prime = true;
            for (int j = 2; j*j <= i; j++)
            {
                if (i % j == 0) prime = false;
            }
            writer.write(prime ? (i + "\n") : "");
        }
    }
}