// Nicholas Rolland
// Assignment 1
// COP4520, Spring 2024, Prof. Juan Parra
// 1/26/2024

import java.util.*;
import java.io.*;

public class primesbasic
{
    // Some global trackers. not best practice ik
    public static int sumPrimes = 0;
    public static int numPrimes = 0;

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


            writer.close();

            final long end = System.nanoTime();

            System.out.println("Runtime: " + (end - start) / 1000000.0 + "ms");
        }
        catch (IOException e)
        {
            e.printStackTrace();
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

    public static void printPrimesFaster(int lowerbound, int upperbound, FileWriter writer) throws IOException
    {
        for (int i = lowerbound; i <= upperbound; i++)
        {
            if (isPrime(i))
            {
                sumPrimes += i;
                numPrimes++;
                writer.write(i + "\n");
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

        for (int i = 6; i*i <= n; i += 6)
        {
            // Test divisibility by 6k+1 and 6k-1
            if ((n+1) % i == 0 || (n-1) % i == 0) return false;
        }
        return true;
    }
}