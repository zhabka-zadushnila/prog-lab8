package utils;

import exceptions.WrongRequestException;
import structs.*;
import structs.wrappers.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class RequestResponseTool {
    private static final Logger logger = Logger.getLogger(RequestResponseTool.class.getName());

    static public boolean sendRequest(SocketChannel channel, Packet packet) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(packet);
            objectStream.flush();
            byte[] data = byteStream.toByteArray();
            logger.fine("Object written");

            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            lengthBuffer.putInt(data.length);
            lengthBuffer.flip();
            channel.write(lengthBuffer);
            logger.fine("Packet size written");

            channel.write(ByteBuffer.wrap(data));
            logger.fine("Packet sent");
            return true;
        } catch (IOException e) {
            logger.warning("Произошла ошибка во время отправки запроса");
            e.printStackTrace();
            return false;
        }
    }

    static public Packet getRequest(SocketChannel channel) throws WrongRequestException {
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
            logger.fine("Got request length");

            while (buffer.hasRemaining()) {
                if (channel.read(buffer) == -1) {
                    throw new EOFException("Unexpected end of stream");
                }
            }

            buffer.flip();
            logger.fine("Got request");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            Object object = objectInputStream.readObject();
            logger.fine("Got request object");
            if (object instanceof Packet) {
                return (Packet) object;
            } else {
                System.out.println("damn thats bad packet" + object.toString());
                throw new WrongRequestException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Input/output error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received corrupted or unknown object: " + e.getMessage());
        }
        return null;
    }

}
