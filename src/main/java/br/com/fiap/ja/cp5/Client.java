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

            // Gera as chaves RSA do cliente.
            RSAUtils rsaUtils = new RSAUtils();
            System.out.println("Chaves RSA do cliente geradas.");

            // Recebe a chave pública do servidor.
            PublicKey chavePublicaServidor = Conexao.receberChave(socket);
            System.out.println("Chave pública do servidor recebida.");

            // Envia a chave pública do cliente para o servidor.
            Conexao.enviarChave(socket, rsaUtils.getPublicKey());
            System.out.println("Chave pública enviada ao servidor.");

            // Solicita mensagem do usuário.
            Scanner input = new Scanner(System.in);
            System.out.print("Digite a sua mensagem: ");
            String textoRequisicao = input.nextLine();

            // Cifrar a mensagem com RSA e enviar para o servidor em blocos.
            byte[] textoCifrado = rsaUtils.encryptEmBlocos(textoRequisicao, chavePublicaServidor);
            Conexao.enviar(socket, Base64.getEncoder().encodeToString(textoCifrado));
            System.out.println("Mensagem cifrada enviada ao servidor.");

            // Receber e decifrar a resposta do servidor.
            String respostaCifrada = Conexao.receber(socket);
            if (!respostaCifrada.isEmpty()) {
                String respostaDecifrada = rsaUtils.decryptEmBlocos(Base64.getDecoder().decode(respostaCifrada), rsaUtils.getPrivateKey());
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
