import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    final static int ServerPort = 3005;
    public static void main(String args[]) throws IOException {
        Scanner scn = new Scanner(System.in);
        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
        System.out.println("Client is connedted to the chat server.");
        // obtaining input and out streams
        // 프로그램에서 데이터를 기록할 때 Stream으로 저장하면 Binary로 저장되고, Reader, writing은 아스키 코드로 저장됨.
        // 데이터를 기록하고, 읽어들이는데 타입별로 변환해 기록하거나 읽을 수 있음. 수 있음.
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        // send client's name
        System.out.print("Name : ");
        String name = scn.nextLine();
        dos.writeUTF(name); // 서버로 UTF 형태로 송신함
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() { // runable 인터페이스를 구현해 스레드를 동작하게 만듬
            @Override
            public void run() {
                while (true) {
                    // read the message to deliver.
                    //System.out.print("Your msg(who#msg) : ");
                    String msg = scn.nextLine();
                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                        if(msg.equals("logout")) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    dis.close();
                    dos.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        if(msg == null) break;
                        System.out.println(msg);
                    } catch (IOException e) {
                        try {
                            s.close();
                            dis.close(); dos.close();
                            break;
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }
        });
        sendMessage.start();
        readMessage.start();
    }
}
