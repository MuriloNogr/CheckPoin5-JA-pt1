package br.com.fiap.ja.cp5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

public class Conexao {

    public static String receber(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] infoBytes = new byte[4096];
        int bytesLidos;

        socket.setSoTimeout(10000); // Define um tempo limite para a leitura.
        while ((bytesLidos = in.read(infoBytes)) != -1) {
            buffer.write(infoBytes, 0, bytesLidos);
            if (bytesLidos < 4096) {
                break;
            }
        }

        return new String(buffer.toByteArray()).trim();
    }

    public static PublicKey receberChave(Socket socket) throws Exception {
        String chaveBase64 = receber(socket);
        byte[] chaveBytes = Base64.getDecoder().decode(chaveBase64);
        return RSAUtils.bytesParaChave(chaveBytes);
    }

    public static void enviarChave(Socket socket, PublicKey chave) throws IOException {
        OutputStream out = socket.getOutputStream();
        String chaveBase64 = Base64.getEncoder().encodeToString(chave.getEncoded());
        out.write((chaveBase64 + "\n").getBytes());
        out.flush();
    }

    public static void enviar(Socket socket, String textoRequisicao) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write((textoRequisicao + "\n").getBytes());
        out.flush();
    }
}
