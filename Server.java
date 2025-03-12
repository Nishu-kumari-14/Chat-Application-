
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
Bidirectional Communication:

Once the server has the input and output streams from the Socket, the server can read data from the client (via the InputStream)
and write responses back to the client (via the OutputStream).
The client's OutputStream (via its Socket object) is connected to the server's InputStream,
and the client's InputStream is connected to the server's OutputStream.
*/
class Server {
    ServerSocket serversocket;
    Socket socket;
    BufferedReader bufferReadMsg;
    PrintWriter sendMsg;

    Server() {
        System.out.println("Server Started");
        try {
            serversocket = new ServerSocket(7777);
            System.out.println("Waiting for client request");
            socket = serversocket.accept();         /*here socket object has been binded to the specific client
                                                    when server accepts request from a client ,server's inputStream binds with
                                                     client's OutputStream and client's inputStream to server's OutputStream*/
            bufferReadMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sendMsg = new PrintWriter(socket.getOutputStream());
           

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reading started");
            try {

                while (!socket.isClosed()) {
                    String msg = bufferReadMsg.readLine();
                    if (msg.equals("exit")) {
                        socket.close();

                    }

                    System.out.println("Client:" + msg);
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        };
        new Thread(r1).start();

    }


    void startWriting() {
        Runnable r2 = () -> {
            BufferedReader Msg;
            System.out.println("Writing started");
            try {
                while (!socket.isClosed()) {
                    Msg = new BufferedReader(new InputStreamReader(System.in));
                    String content = Msg.readLine();
                    sendMsg.println(content);
                    sendMsg.flush();
                    if(content.equals("exit")){
                        socket.close();

                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startReading();
        server.startWriting();
    }

}