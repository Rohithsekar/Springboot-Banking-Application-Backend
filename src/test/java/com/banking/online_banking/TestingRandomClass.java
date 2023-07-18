package com.banking.online_banking;

import com.banking.online_banking.utilities.RandomNumberGenerator;

import java.util.Random;

public class TestingRandomClass {

    public static long RandomNumberGenerator(long bound){
        Random randomNumber = new Random();
        long randomLong = randomNumber.nextLong(bound);
        return randomLong;
    }

    public static void main(String[] args) {
       long randomNumberGenerated = RandomNumberGenerator(99);
        System.out.println(randomNumberGenerated);
    }
}
