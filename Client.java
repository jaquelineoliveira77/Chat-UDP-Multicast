/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatmult;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 *
 * @author Jaqueline
 */
public class Client {

    private static final String EXIT = "Exit";
    static String username;
    static volatile boolean finished = false;
    static String group;
    int port;
    //private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println();
        System.out.println("Ola, voce deseja solicitar acesso a sala de bate-papo?");
        System.out.println("1 - SIM | 2 - NAO");
        int answer = input.nextInt(); //answer = responda

        if (answer == 1) {
            try {
                // 224.1.1.1
                InetAddress group = InetAddress.getByName("239.0.0.0");
                //50000
                int port = Integer.parseInt("4200");

                // 224.1.1.1
                /*System.out.println("Digite o endereco IP do grupo: ");
                 group = input.nextLine();

                //50000
                System.out.println("Digite o endereco da porta do grupo: ");
                int port = input.nextInt();
                */
                 /*
                InetAddress fornece métodos para obter o endereço IP de qualquer nome de host. 
                Um endereço IP é representado por um número não assinado de 32 ou 128 bits. 
                InetAddress pode lidar com endereços IPv4 e IPv6.
                 */
                 
                System.out.println();
                System.out.println(" ------ O Chat ta ON! -------");
                System.out.println("---Seja bem-vindo ...");

                System.out.println("Você esta conectado no HOST: " + group.getHostAddress() + " - PORTA: " + port + "\n");
                System.out.println("Por favor, insira o seu username: ");
                username = input.next();

                //criando multicast socket/ pontos de conexão
                MulticastSocket socket = new MulticastSocket(port);
                socket.setTimeToLive(0);
                /*
                Configura o período de tempo padrão em milissegundos a partir de seu tempo de envio que uma mensagem de produção 
                deve ser retida pelo sistema de mensagens.
                 */
                socket.joinGroup(group);
                //criando Thread
                Thread thread = new Thread(new Server(socket, group, port));
                thread.start();
                System.out.println("" + username + " acabou de se conectar! \n");
                System.out.println("Voce esta conectado(a), esta tudo pronto para conversar com seus amigos!");

                while (true) {
                    String message;
                    message = input.nextLine();
                    LocalDate data = LocalDate.now();
                    LocalTime hora = LocalTime.now();
                    if (message.equalsIgnoreCase(Client.EXIT)) {
                        finished = true;
                        socket.leaveGroup(group);
                        socket.close();
                        break;
                    }
                    if (!message.isEmpty()) {
                        message = username + ": " + message;
                        //Mostrando o usuário, data e hora e a mensagem
                    }

                    //Mostrando o usuário, data e hora e a mensagem
                    System.out.println("Usuario: " + username + "\nData: " + data + "\nHora: " + hora + "\nMensagem: " + message + "\n");
                    byte[] buffer = message.getBytes();

                    // DatagramSockets vai ser usado para enviar e receber pacotes pela Internet.
                    DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
                    socket.send(datagram); //enviando a mensagem

                }
            } catch (SocketException e) {
                System.out.println("ERROR: Não foi possivel conectar-se! Certifique-se que o IP inserido esta valido.");
            } catch (IOException e) {
                System.out.println("ERROR: Houve alguma instabilidade no sistema.");
            }
        } else {
            System.out.println();
            System.out.println("Poxa, que pena! Você optou por não entrar na sala de bate-papo, então você está sendo desconectado...");
        }
    }
}
