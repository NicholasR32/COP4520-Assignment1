// Nicholas Rolland
// Assignment 1
// COP4520, Spring 2024, Prof. Juan Parra
// 1/26/2024

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.*;

// ANSWER: there are 5761455 primes < 10^8

// Apparently, the Java docs for Thread use PrimeThread as an example class. What a coincidence
// https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
public class PrimeThread extends Thread
{
    // Must use AtomicIntegers since primitive ints aren't thread safe
    public static AtomicLong numPrimes = new AtomicLong(0);
    public static AtomicLong sumPrimes = new AtomicLong(0);

    public static FileWriter writer = null;

    private int lowerbound;
    private int upperbound;

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
            int numThreads = 8;
            int upperbound = 100000000;
            int numsPerThread = upperbound / numThreads;
            for (int i = 0; i < numThreads; i++)
            {
                int low = i * numsPerThread;
                int high = (i+1) * (numsPerThread) - 1;

                PrimeThread thread = new PrimeThread(low, high);
                thread.start();
                System.out.println("Thread " + i + " started; checking [" + thread.lowerbound + "," + thread.upperbound + "]");
                //System.out.println("Thread " + i + " done");
            }

            writer.close();

            final long end = System.nanoTime();

            System.out.println((end - start) / 1000000.0 + " " + numPrimes + " " + sumPrimes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public PrimeThread(int lowerbound, int upperbound)
    {
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
    }

    public void run()
    {
        int numPrimesFound = 0;
        long sumPrimesFound = 0;
        for (int i = this.lowerbound; i <= this.upperbound; i++)
        {
            if (isPrime(i))
            {
                numPrimesFound++;
                sumPrimesFound += i;
                try
                {
                    writer.write(i + "\n");
                }
                catch (IOException e)
                {}
            }
        }
        System.out.println("found " + numPrimesFound);
        numPrimes.addAndGet(numPrimesFound);
        System.out.println("total: " + numPrimes);
        sumPrimes.addAndGet(sumPrimesFound);
        System.out.println("total sum: " + sumPrimes);
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

        for (int i = 6; i*i <= n; i += 6)
        {
            // Test divisibility by 6k+1 and 6k-1
            if (n % (i+1) == 0 || (n % (i-1) == 0)) return false;
        }
        return true;
    }
}