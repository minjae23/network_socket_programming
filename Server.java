package socket;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.InetAddress;

public class Server {
	//Create 20 thread pools
    private static ExecutorService threadPool = Executors.newFixedThreadPool(20);

    public static Data calc(String exp) {
    	
        Data r = new Data();// store response type and value in Data class
        
        StringTokenizer st = new StringTokenizer(exp, " ");//Receive Space
        if (st.countTokens() != 3) {// if over 3 or under 3 error return;
            r.setReturnType("Error");
            r.setErrorMessage("arguments is not unsuitable");
            return r;
        }

        r.setReturnType("Answer");
        //----- The part of processing requests sent by the client------//
        String opcode = st.nextToken(); 
        int op1 = Integer.parseInt(st.nextToken());
        int op2 = Integer.parseInt(st.nextToken());

        switch (opcode) {
            case "ADD":
                r.setResponse(op1 + op2);
                break;
            case "SUB":
                r.setResponse(op1 - op2);
                break;
            case "MUL":
                r.setResponse(op1 * op2);
                break;
            case "DIV":
                r.setResponse(op1 / op2);
                break;
            default:
            	r.setErrorMessage("invalid calculate code");
        }
        return r;
    }

    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
        	InetAddress ip = Inet4Address.getLocalHost();
            int port = 9503;
            listener = new ServerSocket(port); // create server socket 
            System.out.println("연결을 기다리고 있습니다.....");
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Server_config.txt"))) {
                bufferedWriter.write(ip.getHostAddress() + "\n");
                bufferedWriter.write(Integer.toString(port) + "\n");
            } catch (IOException e) {
                System.out.println("Server_config.txt error: " + e.getMessage());
            }

            while (true) {
            	
                Socket socket = listener.accept(); // wait client request;
                System.out.println("연결되었습니다.");
                
                threadPool.submit(() -> { //Processing client requests in parallel
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        while (true) {
                            String inputMessage = in.readLine();
                            if (inputMessage.equalsIgnoreCase("close")) {//When the client input close, one thread server shuts down
                                System.out.println("closed by client");
                                break; 
                            }
                            System.out.println(inputMessage); 
                            Data data = calc(inputMessage); 

                            out.write(data.responseToClinet() + "\n"); // calculate result response to client
                            out.flush();
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            socket.close(); 
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (listener != null)
                    listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
