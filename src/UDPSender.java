import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPSender {
    private static final Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();   // 자동으로 할당되게 놔둠
            InetAddress addr = InetAddress.getByName("localhost");  // UDPReceiver 의 주소임
            while (true) {
                System.out.print("Message: ");
                String msg = scn.nextLine();
                byte[] buffer = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, 9005); // 전송 패킷
                socket.send(packet);    // packet 의 버퍼 내용을 읽어 소켓을 통해 외부로 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
