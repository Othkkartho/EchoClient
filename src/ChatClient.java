import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        SocketChannel client;
        //ByteBuffer buffer;

        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
            //buffer = ByteBuffer.allocate(256);

            while(true) {
                System.out.print("message : ");
                String msg = scn.nextLine();
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                String response = null;

                try {
                    client.write(buffer);
                    if(msg.equals("quit")) break;
                    buffer.clear();
                    client.read(buffer);
                    response = new String(buffer.array()).trim();
                    System.out.println("response = " + response);
                    buffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}