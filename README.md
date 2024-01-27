# COP4520-Assignment1
## Instructions
1. Download `Main.java`.
2. Compile and run the program in one command by typing: `java Main.java`.
3. Alternatively, compile with `javac Main.java` and then run with `java Main`.
4. Once complete, the program will create a file called `primes.txt` on your computer, and write the results to it.

The following will be printed to `primes.txt`:

\<execution time, in seconds\> \<total number of primes found\> \<sum of all primes found\>

\<top ten maximum primes, listed in order from lowest to highest\>
## Assignment 1 - Finding prime numbers using concurrent threads
This program uses 8 concurrent threads to find all primes between 1 and 10^8.

The use of multithreading increased the overall computation speed by around 4-5 times, given that a single thread could complete the task in 23-26 seconds, while 8 threads can complete it in 5-6 seconds (at least on my machine, your test run may be slower, but hopefully under 30 seconds).

## Parallelization
This program splits up the range of numbers to search into equal length intervals among each thread, in this case 10^8 numbers divided by 8 threads. Thread 0 will check the first group of 12500000 numbers, Thread 1 the second group, etc. While the amount of work is technically larger for the last few threads, the difference shouldn't be too significant as most of the numbers checked are of the same magnitude.

## Prime Finding Method
The primality test used is a common improvement of naive trial division, using the fact that every prime is of the form 6k+1, or 6k-1,
for positive integer k.

Proof: Any natural number n can be expressed in the form 6k+r, where r must be 1,2,3,4, or 5.
* If r = 0, 2, or 4, n must be even, since r is even and 6 is even.
* In other words, 6k, 6k+2, and 6k+4 are all even, and therefore composite.
* If r = 3, n must be a multiple of 3, and therefore composite.
* Therefore, all primes above 3 must be of the form 6k+1 or 6k+5.

Another way to implement trial division is to divide n by every prime below or equal to sqrt(n), instead of every number, since if you check divisibility by a prime, checking divisibility by all its multiples is unnecessary. Using a list of known primes to find higher primes is also the basis for the Sieve of Eratosthenes.

However, this requires building a list of known primes with no gaps, which would be painful for me to synchronize.

Doing trial division by every number of the form 6k+1 or 6k-1
guarantees we test divisibility by every prime <= sqrt(n).

[Source](https://en.wikipedia.org/wiki/Primality_test#Simple_methods)

