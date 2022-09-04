/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatmult;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Jaqueline
 */
class Server implements Runnable {

    private int port;
    private InetAddress group;
    private MulticastSocket socket;  //Quando dois computadores necessitam manter uma comunicação, cada um deles utiliza um socket
    //socket representa um ponto de conexão
    private static final int MAX_LEN = 1000;

    public Server(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket; //referenciando a própria variável socket /o this sempre irá referenciar os atributos da classe
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        /*Cada thread é associada com uma instância da classe Thread. A aplicação que cria instância de Thread deve fornecer o código a ser executado na thread. 
        run(): é o método que executa as atividades de uma thread. Quando este método finaliza, a thread também termina. */

        while (!Client.finished) {
            byte[] buffer = new byte[Server.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            /*DatagramSockets são mecanismos Java para comunicação de rede via UDP em vez de TCP. 
            Java fornece DatagramSocket para se comunicar por UDP em vez de TCP. Também é construído com base no IP. 
            DatagramSockets pode ser usado para enviar e receber pacotes pela Internet. */
            String message;
            try {
                //Em resumo, o try/catch serve para tratar comportamentos inesperados
                socket.receive(datagram);  //O método receive() da classe Java DatagramSocket recebe um pacote de datagrama do soquete. 
                //Este método retorna o buffer do datagrama Packet (onde o pacote contém o endereço IP e o número da porta da máquina do remetente) quando ele é preenchido com os dados recebidos, é o soquete fechado ou não.
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!message.startsWith(Client.username)) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                //catch marcam um bloco de declarações para testar (try), e especifica uma resposta, caso uma exceção seja lançada
                System.out.println("" + Client.username + " está sendo desconectado...");
            }
        }
    }
}
