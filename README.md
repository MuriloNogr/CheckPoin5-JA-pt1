
# Checkpoint 5 - Java Advanced - Parte 1

Este projeto é uma aplicação que simula a comunicação entre um **Cliente** e um **Servidor** utilizando **Sockets** e criptografia RSA. A troca de mensagens entre cliente e servidor é feita de forma segura, com chaves públicas e privadas geradas em cada lado, e a comunicação é cifrada e decifrada com RSA.

## Visão Geral do Projeto

A aplicação permite que um cliente se conecte a um servidor, ambos geram chaves RSA (pública e privada), e trocam mensagens cifradas. As principais funcionalidades do sistema são:

- Geração de chaves RSA no cliente e no servidor.
- Troca de chaves públicas.
- Envio e recebimento de mensagens cifradas entre cliente e servidor.
- Decifragem das mensagens recebidas.

### Principais Tecnologias Utilizadas

- **Java 17**
- **Sockets**: Para comunicação entre cliente e servidor.
- **RSA**: Para criptografia e segurança de mensagens.
- **Base64**: Para codificação das mensagens cifradas.

## Estrutura do Projeto

O projeto segue uma estrutura básica de comunicação cliente-servidor, com classes separadas para gerenciar a criptografia e a comunicação via sockets.

- **Server**: Classe responsável por iniciar o servidor, gerar chaves RSA e se comunicar com o cliente.
- **Client**: Classe responsável por se conectar ao servidor, gerar suas próprias chaves RSA e enviar/receber mensagens.
- **RSAUtils**: Classe utilitária que implementa a geração de chaves RSA, cifragem e decifragem das mensagens.
- **Conexao**: Classe responsável por enviar e receber dados (chaves e mensagens) através dos sockets.

## Processo de Comunicação

1. O servidor inicia na porta `9600` e aguarda conexões.
2. O cliente se conecta ao servidor.
3. Ambos, cliente e servidor, geram pares de chaves RSA (pública e privada).
4. O servidor envia sua chave pública para o cliente, que a utiliza para cifrar a mensagem.
5. O cliente envia sua chave pública para o servidor.
6. O cliente envia uma mensagem cifrada com a chave pública do servidor.
7. O servidor decifra a mensagem com sua chave privada e responde com uma mensagem cifrada.
8. O cliente decifra a resposta com sua chave privada.

### Design Patterns

- **Factory Pattern**: Utilizado indiretamente na geração de chaves RSA por meio de `KeyPairGenerator`.
- **Singleton Pattern**: No uso dos sockets, garantindo uma única conexão cliente-servidor por execução.
- **Strategy Pattern**: A forma como a comunicação e criptografia são processadas em blocos pode ser vista como uma estratégia para lidar com grandes volumes de dados.

## Exemplo de Execução no Servidor

```java
public static void main(String[] args) {
    try {
        new Server().rodarServidor();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

Ao rodar o servidor, ele gera as chaves RSA e aguarda uma conexão. Após a conexão, ele realiza a troca de chaves públicas com o cliente e espera receber uma mensagem cifrada.

## Exemplo de Execução no Cliente

```java
public static void main(String[] args) {
    try {
        new Client().comunicarComServidor();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

Ao rodar o cliente, ele se conecta ao servidor, gera suas chaves RSA e envia uma mensagem cifrada ao servidor, que responde com outra mensagem cifrada.

### Planilha de dados RSA

![image](https://github.com/user-attachments/assets/bbe00786-f399-42b8-9f16-d083bf61a10a)


### Teste com Drexel RSA Express Encryption/Decryption Calculator 

![img](https://github.com/user-attachments/assets/7f62a51a-4295-4065-a499-c3699de81154)

### Exemplo de Troca de Mensagens

1. Cliente gera mensagem:
   ```plaintext
   Mensagem original: "Olá, Servidor!"
   ```

2. Cliente envia mensagem cifrada:
   ```plaintext
   Mensagem cifrada: "Z3V5Jk...=="
   ```

3. Servidor decifra a mensagem:
   ```plaintext
   Mensagem decifrada: "Olá, Servidor!"
   ```

4. Servidor responde ao cliente:
   ```plaintext
   Mensagem original: "Olá, Cliente!"
   ```

5. Cliente recebe e decifra a resposta:
   ```plaintext
   Mensagem decifrada: "Olá, Cliente!"
   ```

## Diagrama UML

![RSAUML](https://github.com/user-attachments/assets/f0a39d02-11de-4338-82d1-73db5c3c0974)

## Contribuidores

- **Luis Fernando Menezes Zampar** - RM 550531
- **Diogo Fagioli Bombonatti** - RM 551694
- **Murilo Nogueira** - RM 89162
- **Gabriel Galdino da Silva** - RM 550711
