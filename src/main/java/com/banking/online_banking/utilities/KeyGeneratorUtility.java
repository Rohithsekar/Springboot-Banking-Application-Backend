package com.banking.online_banking.utilities;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {
    private static Logger log = LoggerFactory.getLogger(KeyGeneratorUtility.class);

    public static KeyPair generateRsaKey(){

        KeyPair keyPair;

        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch(Exception e){
            throw new IllegalStateException();
        }

        return keyPair;
    }
}
