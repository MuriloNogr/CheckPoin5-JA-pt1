package br.com.fiap.ja.cp5;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class Server {
    public void rodarServidor() throws Exception {
        System.out.println("Servidor iniciado e aguardando conexões...");
        try (ServerSocket serversocket = new ServerSocket(9600)) {
            while (true) {
                try (Socket socketClient = serversocket.accept()) {
                    System.out.println("Cliente conectado: " + socketClient.getInetAddress());

                    RSAUtils rsaUtils = new RSAUtils();
                    System.out.println("Chaves RSA do servidor geradas.");
                    System.out.println("Chave Pública do Servidor: " + Base64.getEncoder().encodeToString(rsaUtils.getPublicKey().getEncoded()));
                    System.out.println("Chave Privada do Servidor: " + Base64.getEncoder().encodeToString(rsaUtils.getPrivateKey().getEncoded()));
                    System.out.println("Valores P e Q do Servidor: P = " + rsaUtils.getP() + ", Q = " + rsaUtils.getQ());

                    Conexao.enviarChave(socketClient, rsaUtils.getPublicKey());
                    System.out.println("Chave pública enviada ao cliente.");

                    PublicKey chavePublicaCliente = Conexao.receberChave(socketClient);
                    System.out.println("Chave pública do cliente recebida: " + Base64.getEncoder().encodeToString(chavePublicaCliente.getEncoded()));

                    String mensagemCifrada = Conexao.receber(socketClient);
                    if (!mensagemCifrada.isEmpty()) {
                        String mensagemDecifrada = rsaUtils.decryptEmBlocos(Base64.getDecoder().decode(mensagemCifrada), rsaUtils.getPrivateKey());
                        System.out.println("Mensagem cifrada recebida: " + mensagemCifrada);
                        System.out.println("Mensagem recebida do cliente: " + mensagemDecifrada);
                    } else {
                        System.out.println("Nenhuma mensagem recebida do cliente.");
                    }


                    Scanner input = new Scanner(System.in);
                    System.out.print("Digite a resposta ao cliente: ");
                    String resposta = input.nextLine();

                    byte[] respostaCifrada = rsaUtils.encryptEmBlocos(resposta, chavePublicaCliente);
                    System.out.println("Resposta original: " + resposta);
                    System.out.println("Resposta cifrada: " + Base64.getEncoder().encodeToString(respostaCifrada));
                    Conexao.enviar(socketClient, Base64.getEncoder().encodeToString(respostaCifrada));
                    System.out.println("Resposta cifrada enviada ao cliente.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Server().rodarServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}