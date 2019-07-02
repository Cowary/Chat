package ru.airvary.others;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.math.BigInteger;
import java.security.*;

public class SecurityChat {
    KeyPairGenerator parigen;
    KeyPair keyPair;
    public Key publicKey;
    Key privateKey;
    Cipher cipherRSA;
    Cipher decriptCipher;

    KeyGenerator keyGeneratorAES;
    SecretKey secretKeyAES;
    Cipher cipherAES;
    Cipher decriptCipherAES;

    SecureRandom random;
    IvParameterSpec iv;


    public SecurityChat(){
      /*
        try {
            cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //decriptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            parigen = KeyPairGenerator.getInstance("RSA");
            keyPair = parigen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        */
        random = new SecureRandom();
        /*
        try {
            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        System.out.println(publicKey.toString());
        System.out.println(privateKey.toString());
        */
    }

    public Key genRSA(){
        try {
            cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            parigen = KeyPairGenerator.getInstance("RSA");
            keyPair = parigen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            cipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
            System.out.println(publicKey.toString());
            System.out.println(privateKey.toString());
            return publicKey;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SecretKey genAESsecurity(){
        try {
            cipherAES = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            decriptCipherAES = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            keyGeneratorAES = KeyGenerator.getInstance("AES");
            keyGeneratorAES.init(256);
            return keyGeneratorAES.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void startAESsecurity(){
        try {
            cipherAES = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            decriptCipherAES = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
    public IvParameterSpec ivGen(){
        byte[] iv_buf = new byte[128/8];
        random.nextBytes(iv_buf);
        System.out.println(iv_buf);
        return new IvParameterSpec(iv_buf);
    }
    public IvParameterSpec CipherAES(SecretKey key){
        try {
            iv = ivGen();
            cipherAES.init(Cipher.ENCRYPT_MODE, key, iv);
            return iv;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void DecryptCipherAES(SecretKey key, IvParameterSpec iv){
        try {
            decriptCipherAES.init(Cipher.DECRYPT_MODE, key, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /*
    public void CipherOn(Key PublicKey){
        try {
            cipherRSA.init(Cipher.ENCRYPT_MODE, PublicKey);
            System.out.println("Where error = " + privateKey.toString());
            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    */
    public byte[] EncryptMessageAES(String str){
        byte[] bytes = null;
        try {
            bytes = cipherAES.doFinal(str.getBytes());
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public byte[] EncryptObjectAES(Object ob){
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(ob);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bytes = cipherAES.doFinal(bytes);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public String DecryptMessageAES(byte[] bytes){
        String message = "Error";
        try {
            byte[] decriptedBytes = decriptCipherAES.doFinal(bytes);
            message = new String(decriptedBytes, "UTF-8");
        } catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Message = " + message);
        return message;
    }
    public Object DecryptObjectAES(byte[] bytes){
        byte[] decriptedBytes = null;
        Object ob = null;
        try {
            decriptedBytes = decriptCipherAES.doFinal(bytes);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(decriptedBytes);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            ob = in.readObject();
            return ob;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ob;
    }

    public byte[] EncryptMessageRSA(String str){
        byte[] bytes = null;
        try {
            bytes = cipherRSA.doFinal(str.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public byte[] EncryptMessageRSA(String message, Key pub_key){
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pub_key);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String DecrytMessageRSA(byte[] bytes){
        String message = "Error";
        try {
            byte[] decriptedBytes = cipherRSA.doFinal(bytes);
            message = new String(decriptedBytes, "UTF-8");
        } catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Message = " + message);
        return message;
    }
    public static String md5(String st){
        MessageDigest messageDigest;
        byte[] dig = new byte[0];
        try{
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            dig = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger integer = new BigInteger(1, dig);
        String hex = integer.toString(16);
        StringBuffer hexMd5 = new StringBuffer(32);
        for (int i = 0, count = 32 - hex.length(); i < count; i++){
            hexMd5.append("0");
        }
        return  hexMd5.append(hex).toString();
    }

    public Key getPublicKey(){
        return publicKey;
    }
}
