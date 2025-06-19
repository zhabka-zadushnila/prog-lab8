package utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class InputTools {


    public static String[] splitLine(String line) {
        return line.trim().split("[ \t]+");
    }


    public static Object parseObjectResponse(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                return null;
            }

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Input/output error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received corrupted or unknown object: " + e.getMessage());
        }
        return null;
    }
}
