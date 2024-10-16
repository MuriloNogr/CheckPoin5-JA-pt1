package br.com.fiap.ja.cp5;

import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    public void comunicarComServidor() throws Exception {
        System.out.println("Iniciando conexão com o servidor...");
        try (Socket socket = new Socket("localhost", 9600)) {
            System.out.println("Conectado ao servidor.");

            RSAUtils rsaUtils = new RSAUtils();
            System.out.println("Chaves RSA do cliente geradas.");
            System.out.println("Chave Pública do Cliente: " + Base64.getEncoder().encodeToString(rsaUtils.getPublicKey().getEncoded()));
            System.out.println("Chave Privada do Cliente: " + Base64.getEncoder().encodeToString(rsaUtils.getPrivateKey().getEncoded()));
            System.out.println("Valores P e Q do Cliente: P = " + rsaUtils.getP() + ", Q = " + rsaUtils.getQ());

            PublicKey chavePublicaServidor = Conexao.receberChave(socket);
            System.out.println("Chave pública do servidor recebida: " + Base64.getEncoder().encodeToString(chavePublicaServidor.getEncoded()));

            Conexao.enviarChave(socket, rsaUtils.getPublicKey());
            System.out.println("Chave pública enviada ao servidor.");

            Scanner input = new Scanner(System.in);
            System.out.print("Digite a sua mensagem: ");
            String textoRequisicao = input.nextLine();

            byte[] textoCifrado = rsaUtils.encryptEmBlocos(textoRequisicao, chavePublicaServidor);
            System.out.println("Mensagem original: " + textoRequisicao);
            System.out.println("Mensagem cifrada: " + Base64.getEncoder().encodeToString(textoCifrado));
            Conexao.enviar(socket, Base64.getEncoder().encodeToString(textoCifrado));
            System.out.println("Mensagem cifrada enviada ao servidor.");

            String respostaCifrada = Conexao.receber(socket);
            if (!respostaCifrada.isEmpty()) {
                String respostaDecifrada = rsaUtils.decryptEmBlocos(Base64.getDecoder().decode(respostaCifrada), rsaUtils.getPrivateKey());
                System.out.println("Resposta cifrada recebida: " + respostaCifrada);
                System.out.println("Servidor respondeu: " + respostaDecifrada);
            } else {
                System.out.println("Nenhuma resposta recebida do servidor.");
            }

            System.out.println("Conexão finalizada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Client().comunicarComServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}