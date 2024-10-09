package br.com.fiap.ja.cp5;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CriptografiaClienteServidor {

    public static PublicKey bytesParaChave(byte[] bytesChave) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytesChave);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static KeyPair gerarChavesPublicoPrivada() throws NoSuchAlgorithmException {
        KeyPairGenerator geradorChave = KeyPairGenerator.getInstance("RSA");
        geradorChave.initialize(2048);
        return geradorChave.generateKeyPair();
    }

    public static String cifrarComRSA(String mensagem, PublicKey chavePublica) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cifrador.init(Cipher.ENCRYPT_MODE, chavePublica);
        byte[] bytesMensagem = mensagem.getBytes();
        byte[] bytesCripto = cifrarEmBlocos(bytesMensagem, cifrador);
        return Base64.getEncoder().encodeToString(bytesCripto);
    }

    public static String decifrarComRSA(String mensagemCifrada, PrivateKey chavePrivada) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cifrador.init(Cipher.DECRYPT_MODE, chavePrivada);
        byte[] bytesMensagemCifrada = Base64.getDecoder().decode(mensagemCifrada);
        byte[] bytesDecifrados = decifrarEmBlocos(bytesMensagemCifrada, cifrador);
        return new String(bytesDecifrados);
    }

    private static byte[] cifrarEmBlocos(byte[] dados, Cipher cifrador) throws Exception {
        int tamanhoBloco = 245;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < dados.length; i += tamanhoBloco) {
            int comprimento = Math.min(tamanhoBloco, dados.length - i);
            byte[] bloco = cifrador.doFinal(dados, i, comprimento);
            outputStream.write(bloco);
        }

        return outputStream.toByteArray();
    }

    private static byte[] decifrarEmBlocos(byte[] dados, Cipher cifrador) throws Exception {
        int tamanhoBloco = 256;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < dados.length; i += tamanhoBloco) {
            int comprimento = Math.min(tamanhoBloco, dados.length - i);
            byte[] bloco = cifrador.doFinal(dados, i, comprimento);
            outputStream.write(bloco);
        }

        return outputStream.toByteArray();
    }
}
