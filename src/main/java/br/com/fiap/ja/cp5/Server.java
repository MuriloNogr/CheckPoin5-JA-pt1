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

                    // Gera as chaves RSA do servidor.
                    RSAUtils rsaUtils = new RSAUtils();
                    System.out.println("Chaves RSA do servidor geradas.");

                    // Envia a chave pública do servidor para o cliente.
                    Conexao.enviarChave(socketClient, rsaUtils.getPublicKey());
                    System.out.println("Chave pública enviada ao cliente.");

                    // Recebe a chave pública do cliente.
                    PublicKey chavePublicaCliente = Conexao.receberChave(socketClient);
                    System.out.println("Chave pública do cliente recebida.");

                    // Recebe mensagem do cliente.
                    String mensagemCifrada = Conexao.receber(socketClient);
                    System.out.println("Mensagem cifrada recebida do cliente: " + mensagemCifrada);
                    String mensagemDecifrada = rsaUtils.decrypt(Base64.getDecoder().decode(mensagemCifrada), rsaUtils.getPrivateKey());
                    System.out.println("Mensagem decifrada do cliente: " + mensagemDecifrada);

                    // Solicita uma resposta para enviar ao cliente.
                    Scanner input = new Scanner(System.in);
                    System.out.print("Digite a resposta ao cliente: ");
                    String resposta = input.nextLine();

                    // Cifra a resposta e envia ao cliente.
                    byte[] respostaCifrada = rsaUtils.encrypt(resposta, chavePublicaCliente);
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
