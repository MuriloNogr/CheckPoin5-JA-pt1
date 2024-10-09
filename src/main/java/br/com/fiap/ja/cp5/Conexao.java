package br.com.fiap.ja.cp5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

public class Conexao {

    public static String receber(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        byte[] infoBytes = new byte[4096];
        int bytesLidos = in.read(infoBytes);

        if (bytesLidos == -1) {
            throw new IOException("Conexão encerrada pelo servidor.");
        }

        return new String(infoBytes, 0, bytesLidos).trim();
    }

    public static PublicKey receberChave(Socket socket) throws Exception {
        String chaveBase64 = receber(socket);
        byte[] chaveBytes = Base64.getDecoder().decode(chaveBase64);
        return CriptografiaClienteServidor.bytesParaChave(chaveBytes);
    }

    public static void enviarChave(Socket socket, PublicKey chave) throws IOException {
        OutputStream out = socket.getOutputStream();
        String chaveBase64 = Base64.getEncoder().encodeToString(chave.getEncoded());
        out.write((chaveBase64 + "\n").getBytes());
        out.flush();
    }

    public static void enviar(Socket socket, String textoRequisicao) throws IOException {
        OutputStream out = socket.getOutputStream();
        String textoBase64 = Base64.getEncoder().encodeToString(textoRequisicao.getBytes());
        out.write((textoBase64 + "\n").getBytes());
        out.flush();
    }
}
