import java.time.LocalDateTime;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private boolean flag=true;
   private ArrayList<ClientMsg> messages = new ArrayList<>();
  
    private String address;
    private int port;
    private boolean running;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void chat(Scanner sc) {
        running = true;

        try {
            // Create a new socket connection for each chat session
            socket = new Socket(address, port);
            System.out.println("Connected");
            System.out.println("you can send 'over' to close chat");
            System.out.println("");
            output = new DataOutputStream(socket.getOutputStream());

            // Start a thread for reading messages from the server
            Thread readThread = new Thread(() -> {
                try (DataInputStream serverInput = new DataInputStream(socket.getInputStream())) {
                    while (running) {
                        try {
                            String line = serverInput.readUTF(); // Read message from server
                            
                            System.out.println("Server: " + line);
                            messages.add(new ClientMsg(line,LocalDateTime.now() ));
                            if (line.equalsIgnoreCase("over")) {
                                running = false; // End reading if "over" is received
                            }
                        } catch (EOFException eof) {
                            System.out.println("Server has closed the connection.");
                            running = false; // End loop on server disconnection
                        } catch (IOException i) {
                            System.out.println("Error reading: " + i.getMessage());
                            running = false; // End loop on IO error
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed: " + e.getMessage());
                }
            });
            readThread.start();

            // Main thread for sending messages to the server
            String line = "";
            while (running && !line.equals("over")) {
                try {
                    line = sc.nextLine();  // Use Scanner passed from main method
                    if (!line.isEmpty()) {
                        output.writeUTF(line);
                        messages.add(new ClientMsg(line, LocalDateTime.now()));
                        if (line.equalsIgnoreCase("over")) {
                            running = false;
                        }
                    }
                } catch (IOException i) {
                    System.out.println("Error writing: " + i.getMessage());
                    running = false; // End loop on write error
                }
            }

            System.out.println("Closing connection");
            System.out.println("");
            readThread.join(); // Wait for the read thread to finish
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch (IOException | InterruptedException i) {
            System.out.println("Connection error: " + i.getMessage());
        } finally {
            try {
                if (output != null){
                    output.close();
                } 
                if (socket != null && !socket.isClosed()){
                    socket.close();
                } 
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void displayMessages() {
        System.out.println("there are total "+messages.size()+" msgs");
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < messages.size(); i++) {
         //   System.out.println(messages.get(i));
         System.out.printf("%-5d | %s%n", (i + 1), messages.get(i));
        }
        flag=false;
    }
    public void sortMsgs(){
        Collections.sort(messages.reversed());
        for (int i = 0; i < messages.size(); i++) {
           
            System.out.printf("%-5d | %s%n", (i + 1), messages.get(i));
           }
    }
    public void seenMsg(){
     if (flag==false) {
        System.out.println("all msgs are seen");
     }else{
        System.out.println(" msgs are not seen");
     }
    }
    public void dltMsgByIndex(){
        Scanner sc=new Scanner(System.in);
        displayMessages();
        System.out.println("\nWhich msg do you want to delete");
        int choice=sc.nextInt()-1;
         messages.remove(choice);
       
         System.out.println("msg deleted successfully");
    }
    public void dltMsgs(){
        Scanner n=new Scanner(System.in);
        System.out.println("Do you want to dlt all msgs at once or dlt msg by index");
        System.out.println("WRite 1 to dlt msg at once");
        System.out.println("WRite 2 to dlt msg by index");
        int choice;
        choice=n.nextInt();
        switch (choice) {
            case 1:
                messages.clear();
                System.out.println("msg deleted successfully");
                break;
            case 2:
                dltMsgByIndex();
                break;
        
            default:
                break;
        }
        
    }
    public static void clearConsole() {
        for (int i = 0; i < 2; i++) {
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Client client = new Client("192.168.202.191", 12345);
        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("*********************************");
        System.out.println("*                               *");
        System.out.println("* ✉️ Welcome to Messaging App ✉️  *");
        System.out.println("*                               *");
        System.out.println("*********************************");
        System.out.println();
        System.out.println("        Start Messaging!");
        System.out.println();
        System.out.println("*********************************");
        while (true) {
            clearConsole();
            System.out.println("Enter 1 to start chat");
            System.out.println("Enter 2 to display messages");
            System.out.println("Enter 3 to dlt messages");
            System.out.println("Enter 4 to sort messages");
            System.out.println("Enter 5 to check status of message");
            System.out.println("Enter 0 to exit");

            choice = sc.nextInt();
            sc.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    client.chat(sc); // Pass Scanner to chat method
                    break;
                case 2:
                    client.displayMessages();
                    break;
                case 3:
                    client.dltMsgs();
                    break;
                case 4:
                  client.sortMsgs();
                    break;
                case 5:
                  client.seenMsg();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    break;
            }
        }
    }
}

