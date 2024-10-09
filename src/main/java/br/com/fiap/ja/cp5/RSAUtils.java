package br.com.fiap.ja.cp5;

import java.security.*;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    private KeyPair keyPair;
    private int p;
    private int q;

    public RSAUtils() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
        this.p = 11; // Exemplo de valor de P
        this.q = 13; // Exemplo de valor de Q
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public int getP() {
        return p;
    }

    public int getQ() {
        return q;
    }

    public byte[] encryptEmBlocos(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataBytes = data.getBytes();

        return processarEmBlocos(dataBytes, cipher, 245);
    }

    public String decryptEmBlocos(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decifrados = processarEmBlocos(data, cipher, 256);
        return new String(decifrados);
    }

    private byte[] processarEmBlocos(byte[] dados, Cipher cipher, int tamanhoBloco) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < dados.length; i += tamanhoBloco) {
            int comprimento = Math.min(tamanhoBloco, dados.length - i);
            byte[] bloco = cipher.doFinal(dados, i, comprimento);
            outputStream.write(bloco);
        }

        return outputStream.toByteArray();
    }

    public static PublicKey bytesParaChave(byte[] bytesChave) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(bytesChave));
    }
}