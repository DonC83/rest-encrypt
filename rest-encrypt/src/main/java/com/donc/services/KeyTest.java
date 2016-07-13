package com.donc.services;

import com.google.common.io.Closeables;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.*;

/**
 * Created by donovan on 6/07/2016.
 */
public class KeyTest {

    public static void main(String[] args) {

        File pvtKey = new File("pvt.key");
        File pubKey = new File("pub.key");
        KeyPair kp = null;
        FileInputStream fis = null;

        if (!pvtKey.exists() && !pubKey.exists()) {
            KeyPairGenerator generator = null;
            try {
                generator = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            kp = generator.generateKeyPair();
            try {
                FileOutputStream fos = new FileOutputStream(new File("pvt.key"));
                fos.write(kp.getPrivate().getEncoded());
                fos = new FileOutputStream(new File("pub.key"));
                System.out.println(kp.getPublic().getEncoded());
                System.out.println(kp.getPublic().getAlgorithm());
                fos.write(kp.getPublic().getEncoded());
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            System.out.println(kp.getPrivate().getEncoded());
            System.out.println(kp.getPublic().toString());
        } else {
            try {
                fis = new FileInputStream(pvtKey);
                byte [] encPvtKey = new byte[fis.available()];
                fis.read(encPvtKey);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encPvtKey);
                System.out.println(keySpec);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PrivateKey pk = kf.generatePrivate(keySpec);

                fis = new FileInputStream(pubKey);
                byte [] encPubKey = new byte[fis.available()];
                fis.read(encPubKey);
                X509EncodedKeySpec pubkKeySpec = new X509EncodedKeySpec(encPubKey);
                System.out.println(pubkKeySpec);
                PublicKey pbKey = kf.generatePublic(pubkKeySpec);

                kp = new KeyPair(pbKey, pk);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                Closeables.closeQuietly(fis);
            }



        }


        try {
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, kp.getPublic());

            String hello = "HelloWorld";

            byte[] encrypted = c.doFinal(hello.getBytes());

            System.out.println(encrypted);

            Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c2.init(Cipher.DECRYPT_MODE, kp.getPrivate());
            byte [] decrypted = c.doFinal(encrypted);

            System.out.println(new String(decrypted));




        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
