package com.tcd.ase.userservice.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordHashing {

    public byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.generateSeed(20);
    }

    public String hash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // We will request 10 iterations from the hashing algorithm and a 512 byte key size
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 10, 512);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();

        String base64Hash = Base64.getMimeEncoder().encodeToString(hash);
        return base64Hash;
    }

    public boolean match(String check, String actual, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return actual.equals(hash(check, salt));
    }
}
