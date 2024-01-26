// Nicholas Rolland
// Assignment 1
// COP4520, Spring 2024, Prof. Juan Parra
// 1/26/2024

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.*;

// ANSWER: there are 5761455 primes < 10^8
// The sum is 279209790387276

// Apparently, the Java docs for Thread use PrimeThread as an example class. What a coincidence
// https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html

public class Main
{
    public static int NUM_THREADS = 8;
    public static int UPPER_BOUND = 100000000;

    public static FileWriter writer = null;

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
                System.out.println("Thread " + i + " started; checking [" + threads[i].lowerbound + "," + threads[i].upperbound + "]");
            }

            int numPrimes = 0;
            long sumPrimes = 0;

            // Iterate over all threads and ensure they finish execution, and sum totals
            try
            {
                for (int i = 0; i < NUM_THREADS; i++)
                {
                    threads[i].join();
                    System.out.println("Thread " + i + " finished:");
                    System.out.println("found " + threads[i].numPrimesFound + " primes");
                    System.out.println("sum: " + threads[i].sumPrimesFound);
                    numPrimes += threads[i].numPrimesFound;
                    sumPrimes += threads[i].sumPrimesFound;
                }
            }
            catch (InterruptedException e)
            {}
            writer.close();

            final long end = System.nanoTime();

            System.out.println((end - start) / 1000000000.0 + "s " + numPrimes + " " + sumPrimes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class PrimeThread extends Thread
{
    public int lowerbound;
    public int upperbound;
    public int numPrimesFound;
    public long sumPrimesFound;

    public PrimeThread(int lowerbound, int upperbound)
    {
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
    }

    public void run()
    {
        for (int i = this.lowerbound; i <= this.upperbound; i++)
        {
            if (isPrime(i))
            {
                this.numPrimesFound++;
                this.sumPrimesFound += i;
            }
        }
    }
    // This primality test uses a common improvement of trial division,
    // using the fact that every prime is of the form 6k+1, or 6k-1,
    // for positive integer k.

    // Proof: Any natural number n can be expressed in the form 6k+r,
    //        and n % 6 yields a remainder r in [0,5].
    // If r = 0, 2, or 4, n must be even, since r is even and 6 is even.
    // In other words, 6k, 6k+2, and 6k+4 are all even, and therefore composite.
    // If r = 3, n must be a multiple of 3, and therefore composite.
    // Therefore, all primes above 3 must be of the form 6k+1 or 6k-1.

    // Method: Another way to implement trial division is to divide n by
    // every prime below or equal to sqrt(n), instead of every number,
    // since if you check divisibility by a prime, checking divisibility
    // by all its multiples is unnecessary.
    // However, this requires building a list of known primes with no gaps,
    // which would be painful for me to synchronize.

    // Doing trial division by every number of the form 6k+1 or 6k-1
    // guarantees we test divisibility by every prime <= sqrt(n).

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