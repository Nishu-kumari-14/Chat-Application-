
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class Client{
    Socket socket;
    BufferedReader bufferReadMsg;

    PrintWriter sendMsg;
    Client(){
        System.out.println("Client started");
        try{
            System.out.println("Request sent to the server");
            socket = new Socket("127.0.0.1",7777);/*this socket object is reffering to a specific server
                                                                 has has been binded to that server*/

            System.out.println("Client connected to the server");
            bufferReadMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;/*return type of socket.getInputStream()
                                                                             is InputStream ,and it read raw bytes from the input stream*/
            sendMsg = new PrintWriter(socket.getOutputStream());
           
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void startWriting() {
        Runnable r1 = () -> {
            BufferedReader msg;
            System.out.println("Writing started");
            try {

               while(!socket.isClosed()){
                   msg= new BufferedReader(new InputStreamReader(System.in));
                   String content = msg.readLine();
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
        new Thread(r1).start();

    }
    void startReading(){
        Runnable r2 = ()->{                      //to see Runnable implementation press ctrl and click on Runnable
            System.out.println("Reading started");
            try{
                while(!socket.isClosed()){
                    String msg = bufferReadMsg.readLine();
                    if(msg.equals("exit")){
                        socket.close();

                    }
                    System.out.println("Server:"+msg);
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startWriting();
        client.startReading();

    }
}


/*
Documentation
if you close socket on client side then client's side socket will be closed it will not close socket on the server side
but when server will try to send meg it will encounter IO Exception or SocketException ....the server should explicitly close the connection
*/