import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 연결 처리, 소캣과 관련된 메소드는 블로킹(끝날 조건이 될때까지 무작정 기다림) 메소드로 동작함.
            Socket socket = new Socket("127.0.0.1", 8200); // 서버에 연결하는 과정
            System.out.println("Connection is successful. \n socket: " + socket);

            // 환영메시지 보내기
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = in.readLine(); // 데이터 수신
            System.out.println(msg);
            /*
            while (true) {
                System.out.print("Your Message: ");
                msg = scanner.next();
                if (msg.equals("Bye"))
                    break;
                out.println(msg);
                out.flush();

                // 서버 데이터 수신
                msg = in.readLine();
                System.out.println("From server: " + msg);
            }
            */

            // 멀티스레딩을 위한 스레드 생성
            Thread sendMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.print("Your Message: ");
                        String msg = scanner.nextLine();
                        if (msg.equals("Bye"))
                            break;
                        out.println(msg);
                        out.flush();
                    }
                    try {
                        // 접속을 종료하고 끝냄
                        socket.close();
                        System.out.println("Connection is ended");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Thread recvMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String msg = null;
                        try {
                            msg = in.readLine();
                            System.out.println("From server: " + msg);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            
            // 스레드 실행
            sendMsg.start();
            recvMsg.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}