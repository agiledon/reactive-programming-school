package zhangyi.training.school.reactiveprogramming.samples.c10k;

import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadServer {
    public static final byte[] RESPONSE = (
            "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: 2\r\n" +
                    "\r\n" +
                    "OK").getBytes();

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(8080, 100);
        while (!Thread.currentThread().isInterrupted()) {
            final Socket client = serverSocket.accept();
            handle(client);
        }
    }

    private static void handle(Socket client) throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                readFullRequest(client);
                client.getOutputStream().write(RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
        }
    }

    private static void readFullRequest(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String line = bufferedReader.readLine();
        while (line != null && !line.isEmpty()) {
            line = bufferedReader.readLine();
        }
    }
}
