
import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDateTime;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    int port;
    private boolean flag=true;
    

    private boolean running;
    private ArrayList<ServerMsg> messages = new ArrayList<>();

    public Server(int port) {
        this.port = port;
        Scanner sc=new Scanner(System.in);
        try {
           
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void chat(int port) {
        running = true;
        try {

            // Starts the server and binds it to a specified port number.

            System.out.println("Server started");
            System.out.println("Waiting for a client...");
            // Waits for a client to connect. When a client connects, it returns a Socket
            // object, which represents the connection to the client.
            socket = server.accept();
            System.out.println("Client accepted");
            System.out.println("you can send 'over' to close chat");
            System.out.println("");
            // in: Reads messages sent by the client
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // out: Sends messages to the client.
            out = new DataOutputStream(socket.getOutputStream());

            // Start a thread for reading messages from the client(I am using lamda
            // expression here)
            Thread readThread = new Thread(() -> {
                String line ="";
                while (running && !line.equals("over")) {
                    try {
                        // read msgs sent by client
                        line = in.readUTF(); 
                       
                        System.out.println("Client: " + line);
                        // add message to arraylist
                       messages.add(new ServerMsg(line, LocalDateTime.now()));
                        if (line.equalsIgnoreCase("over")) {
                            running = false;
                        }
                    } catch (IOException i) {
                        System.out.println("Error reading: " + i.getMessage());
                        break;
                    }
                }
            });
            // starts a new thread after executing previous code
            readThread.start();

            // Main thread for sending messages to the client
            DataInputStream consoleInput = new DataInputStream(System.in);
            String line = "";
            while (running && !line.equals("over")) {
                try {
                    line = consoleInput.readLine();
                    out.writeUTF(line);
                    messages.add(new ServerMsg(line, LocalDateTime.now()));

                    if (line.equalsIgnoreCase("over")) {
                        running = false; // Stop both threads if server sends "over"
                    }
                } catch (IOException i) {
                    System.out.println("Error writing: " + i.getMessage());
                }
            }
           
            System.out.println("Closing connection");
            System.out.println("");
            socket.close();
            in.close();
            out.close();

        } catch (IOException i) {
            System.out.println("Server error: " + i.getMessage());
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
    public void seenMsg(){
        if (flag==false) {
            System.out.println("all msgs are seen");
        }else{
            System.out.println("msgs are not seen");
        }
    }
    public void sortMsgs(){
        Collections.sort(messages.reversed());
        for (int i = 0; i < messages.size(); i++) {
           
            System.out.printf("%-5d | %s%n", (i + 1), messages.get(i));
           }
    }
    public static void clearConsole() {
        for (int i = 0; i < 2; i++) {
            System.out.println();
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
    // main method starts here
    public static void main(String[] args) {
        try{
           
            boolean check = true;
            int serverPort=12345;
            Server server = new Server(serverPort);
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
            while (check) {
                clearConsole();
                System.out.println("enter 1 to start chat");
                System.out.println("Enter 2 to display msgs");
                System.out.println("Enter 3 to dlt msgs");
                System.out.println("Enter 4 to sort msgs");
                System.out.println("Enter 5 to check status of msgs");
                System.out.println("Enter 0 to exit");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        server.chat(serverPort);
                        break;
                    case 2:
                        server.displayMessages();
                        break;
                    case 3:
                       server.dltMsgs();
                        break;
                    case 4:
                        server.sortMsgs();
                        break;
                    case 5:
                        server.seenMsg();
                        break;
                    case 0:
                        check=false;
                        break;
    
                    default:
                        break;
                }
            }
    
        }catch(NoSuchElementException i){
            System.out.println("nothing");
        }
      System.out.println("Thank You for using our service"); 
    }
}
