package com.banking.online_banking.utilities;

import java.util.Random;

public class BankingServiceUtilities {

    /*
    The nextInt(int bound) method generates a random integer between 0 (inclusive) and bound (exclusive). In
    this case, bound is set to 900000, which gives a range of integers from 0 to 899999.
    The result could be a number well below a six-digit number . For example, random.nextInt(999999) may return
    0 as the result.

By adding 100000 to the result of random.nextInt(900000), we shift the range to start from 100000 and end at
999999. This ensures that the generated random number will always have six digits.

For example, if random.nextInt(900000) returns 123456, adding 100000 to it would result in 223456, which is a
valid six-digit number.

So, by adding 100000 to the generated random number, we ensure that the final result is within the desired
range of six-digit numbers.
     */

    public static long sixDigitRandomNumber(){
        Random random = new Random();
        return (long)random.nextInt(900000) + 100000; // Generate a random number between 100000 and 999999
    }
}
