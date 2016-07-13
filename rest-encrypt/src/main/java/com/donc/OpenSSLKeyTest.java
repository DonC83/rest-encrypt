package com.donc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by donovan on 13/07/2016.
 */
public class OpenSSLKeyTest {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        File pvtKeyFile = new File("pvt.key");
        File pubKeyFile = new File("pub.key");


        OpenSSLKeyTest ost = new OpenSSLKeyTest();
        try {
            PrivateKey pk = ost.getPrivateKey(pvtKeyFile);
            PublicKey pubKey = ost.getPublicKey(pubKeyFile);
            System.out.println(pk);
            System.out.println(pubKey);
            byte [] encrpted = ost.encrypt(pk, "hello world");
            System.out.println(encrpted);

            String s = ost.decrypt(pubKey, encrpted);
            System.out.println(s);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    byte [] encrypt(PrivateKey pKey, String s)  {
        byte [] results = null;
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, pKey);
            results = c.doFinal(s.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            return results;
        }
    }

    String decrypt(PublicKey pubKey, byte [] bytes) {
        String results = null;
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.DECRYPT_MODE, pubKey);
            results = new String(c.doFinal(bytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            return results;
        }
    }


    PublicKey getPublicKey(File publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (PemReader r = new PemReader(new FileReader(publicKeyFile))) {
            PemObject obj = r.readPemObject();
            EncodedKeySpec keySpec = new X509EncodedKeySpec(obj.getContent());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(keySpec);
        }
    }

    PrivateKey getPrivateKey(File privateKeyFile) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        try (PemReader r = new PemReader(new FileReader(privateKeyFile))) {
            PemObject obj = r.readPemObject();

            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(obj.getContent());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        }
    }

}
