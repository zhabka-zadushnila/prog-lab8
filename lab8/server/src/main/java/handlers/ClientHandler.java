package handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import Interpreters.ServerCommandInterpreter;
import structs.Packet;
import structs.classes.Dragon;
import utils.RequestConstructor;
import utils.RequestResponseTool;

public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final SocketChannel socketChannel;
    private final ServerCommandInterpreter serverCommandInterpreter;
    private final ExecutorService processPool;
    private final ExecutorService writePool;

    public ClientHandler(SocketChannel socketChannel, ServerCommandInterpreter serverCommandInterpreter,
                         ExecutorService processPool, ExecutorService writePool) {
        this.socketChannel = socketChannel;
        this.serverCommandInterpreter = serverCommandInterpreter;
        this.processPool = processPool;
        this.writePool = writePool;
    }


    @Override
    public void run() {
        try {
            handleClient(socketChannel);
        } catch (IOException e) {
            logger.severe("Error handling client: " + e.getMessage());
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                logger.fine("Failed to close socket: " + e.getMessage());
            }


        }
    }

    private void handleClient(SocketChannel socketChannel) throws IOException {
        socketChannel.configureBlocking(true);
        ByteBuffer buffer = ByteBuffer.allocate(1);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead == -1) {
            System.out.println("Client disconnected before sending message.");
            socketChannel.close();
            return;
        }

        if (bytesRead != 1) {
            System.out.println("Strange client hello accepted (" + String.valueOf(bytesRead) + " bytes)");
            logger.info("Strange client hello accepted");
            return;
        }
        buffer.flip();
        if (buffer.get() == 0x42) {
            buffer.clear();
            buffer.put((byte) 0x43);
            buffer.flip();
            socketChannel.write(buffer);
            socketChannel.configureBlocking(false);
            logger.info("New client registered");
        }
        while (socketChannel.isConnected()) {
            Packet packet = RequestResponseTool.getRequest(socketChannel);
            if (packet == null) {
                logger.info("Client disconnected before sending request");
                return;
            }

            processPool.execute(() -> processRequest(packet, socketChannel));
        }
    }

    private void processRequest(Packet packet, SocketChannel socketChannel) {
        try {
            if(packet.isMap()){
                writePool.execute(() -> sendResponse(socketChannel, serverCommandInterpreter.getCollectionManager().getCollection()));
            }else{
                String response = serverCommandInterpreter.executeRequest(packet);
                String result = (response != null) ? response : "Request executed successfully";
                writePool.execute(() -> sendResponse(socketChannel, result));
            }

            
        } catch (Exception e) {
            logger.severe("Error executing command: " + e.getMessage());
            writePool.execute(() -> sendResponse(socketChannel, "Internal server error"));
        }
    }

    private void sendResponse(SocketChannel socketChannel, String message) {
        System.out.println("Sending back: " + message);
        Packet responsePacket = RequestConstructor.createRequest(message);
        RequestResponseTool.sendRequest(socketChannel, responsePacket);
    }
    private void sendResponse(SocketChannel socketChannel, Map<String, Dragon> collection) {
        Packet responsePacket = RequestConstructor.createRequest(collection);
        RequestResponseTool.sendRequest(socketChannel, responsePacket);
    }
}