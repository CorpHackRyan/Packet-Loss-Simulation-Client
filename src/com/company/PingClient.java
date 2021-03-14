package com.company;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.*;


public class PingClient
{
    private static final int total_timeout = 1000;	// amount to time out in milliseconds

    public static void main(String[] args) throws Exception
    {
        // Client side
        int client_port = 2015;

        // Server side
        int server_port = 2014;
        String server_ip = "localhost";

        // Create a datagram socket to send/receive UDP packets
        DatagramSocket socket = new DatagramSocket(client_port);

        int counter = 0;
        int times_to_loop = 10;

        while (counter < times_to_loop) {

            // Get the current time stamp
            Date current_date_time = new Date();

            long start_time = current_date_time.getTime();

            //Generate random sentence in lowercase letters to send to UDP PingServer
            Random letter = new Random();
            StringBuilder sb = new StringBuilder();

            for(int char_count=0; char_count<10; char_count++)
            {
                // Pick a random letter from a-z and append to random_char
                char rand_char = (char)(letter.nextInt(26) + 'a');
                sb.append(rand_char);
            }

            String sentence_str = sb.toString();
            System.out.println("\nRandom sentence sent to server: " + sentence_str);

            byte[] byte_packet = new byte[1024];
            byte_packet = sentence_str.getBytes();

            // Create a UDP packet to send to server.
            DatagramPacket sendPacket = new DatagramPacket(byte_packet, byte_packet.length, InetAddress.getByName(server_ip), server_port);

            // Ping the UDPPingServer by sending a packet
            socket.send(sendPacket);

            // Attempt to receive the packet from UDPPingServer
            try {

                socket.setSoTimeout(total_timeout);


                // Create a new UDP packet, and wait for server to respond
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                socket.receive(response);

                // Timestamp for when we received the packet
                current_date_time = new Date();
                long end_time = current_date_time.getTime();

                System.out.println("Current date/time: " + current_date_time);
                System.out.println("Ping Attempt #: " + counter);

                // Print out RTT (rout trip time in milliseconds)
                System.out.println("RTT Time in milliseconds: " + (end_time - start_time));
                System.out.println("Server IP Address is: " + response.getAddress().getHostAddress());

                String response_msg = new String(response.getData(),0, response.getLength());
                System.out.println("Response message from server: " + response_msg);

            } catch (IOException error) {

                // Print which packet has timed out
                System.out.println("Packet #" + counter + " timed out.\n");
            }

            // Increment the counter and send the next packet
            counter ++;
        }
    }

}