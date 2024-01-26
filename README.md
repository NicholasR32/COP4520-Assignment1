# COP4520-Assignment1
Assignment 1 - Finding prime numbers using concurrent threads

The primality test used is a common improvement of trial division, using the fact that every prime is of the form 6k+1, or 6k-1,
for positive integer k.

Proof: Any natural number n can be expressed in the form 6k+r, where r must be 1,2,3,4, or 5.
* If r = 0, 2, or 4, n must be even, since r is even and 6 is even.
* In other words, 6k, 6k+2, and 6k+4 are all even, and therefore composite.
* If r = 3, n must be a multiple of 3, and therefore composite.
* Therefore, all primes above 3 must be of the form 6k+1 or 6k+5.

Method: Another way to implement trial division is to divide n by every prime below or equal to sqrt(n), instead of every number, since if you check divisibility by a prime, checking divisibility by all its multiples is unnecessary.

However, this requires building a list of known primes with no gaps, which would be painful for me to synchronize.

Doing trial division by every number of the form 6k+1 or 6k-1
guarantees we test divisibility by every prime <= sqrt(n).

