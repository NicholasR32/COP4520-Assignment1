// Nicholas Rolland
// Assignment 1
// COP4520, Spring 2024, Prof. Juan Parra
// 1/26/2024

import java.util.*;
import java.io.*;

// ANSWER: There are 5761455 primes < 10^8
//         Their sum is 279209790387276

public class Main
{
    public static int NUM_THREADS = 8;
    public static int UPPER_BOUND = 100000000;

    public static FileWriter writer = null;
    public static boolean DEBUG = false;

    public static void main(String[] args) throws IOException
    {
        // Handle IOExceptions with files
        try
        {
            // Delete old primes.txt and create new one
            File out = new File("primes.txt");
            if (out.exists()) out.delete();
            out.createNewFile();
            writer = new FileWriter("primes.txt");

            // Record program running time
            final long start = System.nanoTime();

            // Create threads and distribute computation
            PrimeThread[] threads = new PrimeThread[NUM_THREADS];
            int numsPerThread = UPPER_BOUND / NUM_THREADS;
            for (int i = 0; i < NUM_THREADS; i++)
            {
                int low = i * numsPerThread;
                int high = (i+1) * (numsPerThread) - 1;

                threads[i] = new PrimeThread(low, high);
                threads[i].start();
                if (DEBUG) System.out.println("Thread " + i + " started; checking [" + threads[i].lowerbound + "," + threads[i].upperbound + "]");
            }

            int numPrimes = 0;
            long sumPrimes = 0;

            // Iterate over all threads and ensure they finish execution, then sum totals
            try
            {
                for (int i = 0; i < NUM_THREADS; i++)
                {
                    threads[i].join();
                    if (DEBUG) System.out.println("Thread " + i + " finished:");
                    if (DEBUG) System.out.println("found " + threads[i].numPrimesFound + " primes");
                    if (DEBUG) System.out.println("sum: " + threads[i].sumPrimesFound);
                    numPrimes += threads[i].numPrimesFound;
                    sumPrimes += threads[i].sumPrimesFound;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // Stop timer
            final long end = System.nanoTime();
            double runtime = (end - start) / 1000000000.0;
            if (DEBUG) System.out.println((end - start) / 1000000000.0 + " " + numPrimes + " " + sumPrimes);

            // Write runtime and prime totals to primes.txt
            writer.write(runtime + " " + numPrimes + " " + sumPrimes + "\n");

            // Write 10 largest primes in increasing order
            int numLargest = 10;
            Collections.sort(PrimeThread.temp);
            for (int i = PrimeThread.temp.size()-numLargest; i < PrimeThread.temp.size(); i++)
            {
                writer.write(PrimeThread.temp.get(i) + "\n");
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

// Apparently, the Java docs for Thread use PrimeThread as an example class. What a coincidence
// https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
class PrimeThread extends Thread
{
    public int lowerbound;
    public int upperbound;
    public int numPrimesFound;
    public long sumPrimesFound;
    public static ArrayList<Integer> temp = new ArrayList<>();

    public PrimeThread(int lowerbound, int upperbound)
    {
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
    }

    // Main sequence of operations to be run by each thread
    public void run()
    {
        for (int i = this.lowerbound; i <= this.upperbound; i++)
        {
            if (isPrime(i))
            {
                this.numPrimesFound++;
                this.sumPrimesFound += i;

                // Really iffy way of getting the top 10 primes
                if (i > 99990000)
                {
                    PrimeThread.temp.add(i);
                }
            }
        }
    }

    // This primality test implements the algorithm in the README.
    public static boolean isPrime(int n)
    {
        if (n <= 1) return false;
        if (n <= 3) return true;

        // Composite if divisible by 6k,6k+2,6k+3, or 6k+4
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i*i <= n; i += 6)
        {
            // Test divisibility by 6k+1 and 6k-1
            if (n % (i) == 0 || (n % (i+2) == 0)) return false;
        }
        return true;
    }
}