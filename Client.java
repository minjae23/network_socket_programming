package socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
    	
    	Data data = new Data(); // Class that stores response types and values
    	
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        
        try {
        	
        	BufferedReader configReader = new BufferedReader(new FileReader("Server_config.txt"));
            String serverIP = configReader.readLine();
            int serverPort = Integer.parseInt(configReader.readLine());
            configReader.close();

            socket = new Socket(serverIP, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Import data from the server
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// Send data to the server
            
            while (true) {
                System.out.print("(ADD or SUB or MUL or DIV) int int :)>>"); // output
                
                String outputMessage = scanner.nextLine(); // input
                if (outputMessage.equalsIgnoreCase("close")) {
                    out.write(outputMessage + "\n"); // if you input close send data to server and close
                    out.flush();
                    break; 
                }
                data.setRequest(outputMessage); //store string to string-type variable(request) in data class
               
                out.write(data.toString() + "\n"); // to.String -> return request. send data to server
                out.flush();
                String response = in.readLine(); // Receive calculation results from the server
                System.out.println(response);
               
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null)
                    socket.close(); // client socket close
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
