package SDN;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router4 {

    private static ServerSocket serverSocket;
    private static InetAddress host;
    private static final int PORT = 1304;
    private static final int PORT2 = 1300;

    private static Socket link2 = null;

    public static void main(String[] args) {
        System.out.println("Opening port");
        {
            try {
                host = InetAddress.getLocalHost();
                System.out.println("Enter TcpReceiver IP Address:");
//        	  Scanner readIP = new Scanner(System.in);
//        	  host = readIP.nextLine();
            } catch (Exception uhEx) {
                System.out.println("Host ID not found!");
                System.exit(1);
            }

        }
        try {

            serverSocket = new ServerSocket(PORT);  //Step 1.

        } catch (IOException ioEx) {
            System.out.println(
                    "Unable to attach to port for router!");
            System.exit(1);
        }

        handleClient();

    }

    private static String handleClient() {
        Socket link = null;
        String str2 = null;
        try {
            System.out.println("Router4");
            String message;
            do {

                link = serverSocket.accept();
                Scanner input
                        = new Scanner(link.getInputStream());
                message = input.nextLine();
                int[] paths = {3,6,7};

                int[] paths2 = Functions.pastpath(message, paths);

                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(paths2.length);

                int router_ad = paths2[randomInt];

                int NewPort = PORT2 + router_ad;
                link2 = new Socket(host, NewPort);
                if (message.equals("***CLOSE***")) {
                    break;
                }
               
                
                PrintWriter output
                        = new PrintWriter(
                                link.getOutputStream(), true);

                Scanner input2
                        = new Scanner(link2.getInputStream());

                PrintWriter output2
                        = new PrintWriter(
                                link2.getOutputStream(), true);
                System.out.println("message from sender " + message);
                message = message + String.valueOf(router_ad);

                int random = randomGenerator.nextInt(100);
                System.out.println("Generated random number for the packet is: " + random);
                if (random > -1) { //for random probability 20%,each packet has a random number between 0 to 99

                    output2.println(message);

                    String str = input2.nextLine();
                    System.out.println("message from receiver: " + str);
                    output.println(str);
                    link2.close();
                } else {
                    output.println(str2);
                    
                    link2.close();
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Router4.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (!message.equals("***CLOSE***"));

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            try {
                System.out.println(
                        "\n* Closing connections (Router side)*");
                link.close();
                link2.close();
            } catch (IOException ioEx) {
                System.out.println(
                        "Unable to disconnect!");
                System.exit(1);
            }
        }
        return null;
    }

}
