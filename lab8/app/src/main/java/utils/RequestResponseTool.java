package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import exceptions.WrongRequestException;
import structs.Packet;

public class RequestResponseTool {
    static public boolean sendPacket(SocketChannel channel, Packet packet) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(packet);
            objectStream.flush();
            byte[] data = byteStream.toByteArray();

            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            lengthBuffer.putInt(data.length);
            lengthBuffer.flip();
            channel.write(lengthBuffer);

            channel.write(ByteBuffer.wrap(data));
            return true;
        } catch (NotSerializableException e) {
            System.out.println("Вы положили что-то не сериализуемое в реквест");
            return false;
        }
    }

    static public Packet getPacket(SocketChannel channel) throws WrongRequestException {
        try {
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            while (lengthBuffer.hasRemaining()) {
                if (channel.read(lengthBuffer) == -1) {
                    return null;
                }
            }
            lengthBuffer.flip();
            int length = lengthBuffer.getInt();
                ByteBuffer buffer = ByteBuffer.allocate(length);
                



                while (buffer.hasRemaining()) {
                    if (channel.read(buffer) == -1) {
                        throw new EOFException("Unexpected end of stream");
                    }
                }

                buffer.flip();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                Object object = objectInputStream.readObject();

                if (object instanceof Packet) {
                    return (Packet) object;
                } else {
                    throw new WrongRequestException();
                }
        } catch (IOException e) {
            System.out.println("Input/output error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received corrupted or unknown object: " + e.getMessage());
        }
        return null;
    }

}
