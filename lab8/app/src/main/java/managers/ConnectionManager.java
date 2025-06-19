package managers;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.lang.Thread.sleep;

/**
 * A class for managing connections. Gets stuff needed, returns channels
 */
public class ConnectionManager {

    public static SocketChannel connectToServer(String hostname, int port, int MAX_RECONNECT_ATTEMPTS, int RECONNECT_TIMEOUT) {
        for (int i = 0; i < MAX_RECONNECT_ATTEMPTS; i++) {
            try {
                byte BYTE = 0x42;
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(hostname, port));
                channel.configureBlocking(true);
                ByteBuffer buffer = ByteBuffer.allocate(1);
                buffer.put(BYTE);
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
                int bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    System.out.println("Server closed the connection.");
                    channel.close();
                    continue;
                } else if (bytesRead == 0) {
                    System.out.println("Соединение устанавливается. Попытка №" + (i + 1) + "/5");
                    try {
                        channel.close();
                        sleep(RECONNECT_TIMEOUT);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    continue;
                }

                buffer.flip();
                byte serverByte = buffer.get();
                if (serverByte == 0x43) {
                    System.out.println("Соединение установлено");
                    return channel;
                }

            } catch (IOException e) {
                System.out.println("Соединение устанавливается. Попытка №" + (i + 1) + "/5");
                try {
                    sleep(RECONNECT_TIMEOUT);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
        System.out.println("Соединение не установилось спустя 5 попыток. Обрываемся.");
        System.exit(1);
        return null;
    }
}

